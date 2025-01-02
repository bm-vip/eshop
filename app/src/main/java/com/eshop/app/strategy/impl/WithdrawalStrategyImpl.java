package com.eshop.app.strategy.impl;

import com.eshop.app.enums.EntityStatusType;
import com.eshop.app.enums.RoleType;
import com.eshop.app.model.SubscriptionModel;
import com.eshop.app.model.WalletModel;
import com.eshop.app.repository.WalletRepository;
import com.eshop.app.service.SubscriptionPackageService;
import com.eshop.app.service.SubscriptionService;
import com.eshop.app.service.UserService;
import com.eshop.app.strategy.NetworkStrategyFactory;
import com.eshop.app.strategy.TransactionStrategy;
import com.eshop.exception.common.InsufficentBalanceException;
import com.eshop.exception.common.NotAcceptableException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WithdrawalStrategyImpl implements TransactionStrategy {
    private final WalletRepository walletRepository;
    private final SubscriptionService subscriptionService;
    private final SubscriptionPackageService subscriptionPackageService;
    private final NetworkStrategyFactory networkStrategyFactory;
    private final UserService userService;

    @Override
    public void beforeSave(WalletModel model) {
        var balance = walletRepository.calculateUserBalance(model.getUser().getId());
        if(balance.compareTo(model.getAmount()) < 0)
            throw new InsufficentBalanceException();

        var network = networkStrategyFactory.get(model.getNetwork());
        if(model.getStatus().equals(EntityStatusType.Active) && (RoleType.hasRole(RoleType.ADMIN) || network.validate(model))) {
            var currentSubscription = subscriptionService.findByUserAndActivePackage(model.getUser().getId());
            if (currentSubscription.getRemainingWithdrawalPerDay() > 0)
                throw new NotAcceptableException(String.format("Withdrawal is allowed only after %d days", currentSubscription.getRemainingWithdrawalPerDay()));
            if (userService.countAllActiveChild(model.getUser().getId()) < currentSubscription.getSubscriptionPackage().getOrderCount())
                throw new NotAcceptableException(String.format("To withdraw your funds you need to have at least %d referrals.", currentSubscription.getSubscriptionPackage().getOrderCount()));
        }
    }

    @Override
    public void afterSave(WalletModel model) {
        var network = networkStrategyFactory.get(model.getNetwork());
        if(model.getStatus().equals(EntityStatusType.Active) && (RoleType.hasRole(RoleType.ADMIN) || network.validate(model))) {
            var balance = walletRepository.calculateUserBalance(model.getUser().getId());
            var currentSubscription = subscriptionService.findByUserAndActivePackage(model.getUser().getId());
            var nextSubscriptionPackage = subscriptionPackageService.findMatchedPackageByAmount(balance);
            if(nextSubscriptionPackage == null)
                subscriptionService.logicalDeleteById(currentSubscription.getId());
            else if(currentSubscription == null || !currentSubscription.getSubscriptionPackage().getId().equals(nextSubscriptionPackage.getId())) {
                subscriptionService.create(new SubscriptionModel().setSubscriptionPackage(nextSubscriptionPackage).setUser(model.getUser()).setStatus(EntityStatusType.Active));
            }
        }
    }
}
