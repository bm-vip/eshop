package com.eshop.app.strategy.impl;

import com.eshop.app.enums.EntityStatusType;
import com.eshop.app.enums.RoleType;
import com.eshop.app.model.SubscriptionModel;
import com.eshop.app.model.WalletModel;
import com.eshop.app.repository.WalletRepository;
import com.eshop.app.service.SubscriptionPackageService;
import com.eshop.app.service.SubscriptionService;
import com.eshop.app.strategy.NetworkStrategyFactory;
import com.eshop.app.strategy.TransactionStrategy;
import com.eshop.exception.common.InsufficentBalanceException;
import com.eshop.exception.common.NotAcceptableException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class WithdrawalProfitStrategyImpl implements TransactionStrategy {
    private final WalletRepository walletRepository;
    private final NetworkStrategyFactory networkStrategyFactory;
    private final SubscriptionService subscriptionService;
    private final SubscriptionPackageService subscriptionPackageService;

    @Override
    public void beforeSave(WalletModel model) {
        var balance = walletRepository.totalProfitByUserId(model.getUser().getId());
        if (balance.compareTo(model.getAmount()) < 0)
            throw new InsufficentBalanceException();

        var network = networkStrategyFactory.get(model.getNetwork());
        if(model.getStatus().equals(EntityStatusType.Active) && (RoleType.hasRole(RoleType.ADMIN) || network.validate(model))) {
            BigDecimal totalDeposit = walletRepository.totalDepositByUserId(model.getUser().getId()).multiply(BigDecimal.valueOf(0.3));
            BigDecimal totalWithdrawalProfit = walletRepository.totalWithdrawalProfitByUserId(model.getUser().getId());
            BigDecimal allowedWithdrawal = totalDeposit.subtract(totalWithdrawalProfit);
            if (allowedWithdrawal.compareTo(model.getAmount()) < 0)
                throw new NotAcceptableException(String.format("You can withdraw profit up to 30%% of your deposited amount (%d USD).<br/>Your total withdrawal profit is %d USD.<br/> Your allowed withdrawal profit is %d USD.",
                        totalDeposit.longValue(), totalWithdrawalProfit.longValue(), allowedWithdrawal.longValue()));
        }
    }

    @Override
    public void afterSave(WalletModel model) {
        var network = networkStrategyFactory.get(model.getNetwork());
        if (model.getStatus().equals(EntityStatusType.Active) && (RoleType.hasRole(RoleType.ADMIN) || network.validate(model))) {
            var balance = walletRepository.calculateUserBalance(model.getUser().getId());
            var currentSubscription = subscriptionService.findByUserAndActivePackage(model.getUser().getId());
            var nextSubscriptionPackage = subscriptionPackageService.findMatchedPackageByAmount(balance);
            if(nextSubscriptionPackage == null)
                subscriptionService.logicalDeleteById(currentSubscription.getId());
            else if (currentSubscription == null || !currentSubscription.getSubscriptionPackage().getId().equals(nextSubscriptionPackage.getId())) {
                subscriptionService.create(new SubscriptionModel().setSubscriptionPackage(nextSubscriptionPackage).setUser(model.getUser()).setStatus(EntityStatusType.Active));
            }
        }
    }
}
