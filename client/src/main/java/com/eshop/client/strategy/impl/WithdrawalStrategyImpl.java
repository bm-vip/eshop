package com.eshop.client.strategy.impl;

import com.eshop.client.model.SubscriptionPackageModel;
import com.eshop.client.service.SubscriptionService;
import com.eshop.client.enums.EntityStatusType;
import com.eshop.client.model.WalletModel;
import com.eshop.client.repository.WalletRepository;
import com.eshop.client.service.UserService;
import com.eshop.client.strategy.TransactionStrategy;
import com.eshop.exception.common.InsufficentBalanceException;
import com.eshop.exception.common.NotAcceptableException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class WithdrawalStrategyImpl implements TransactionStrategy {
    private final SubscriptionService subscriptionService;
    private final WalletRepository walletRepository;
    private final UserService userService;

    @Override
    public void execute(WalletModel model) {
        BigDecimal balance = walletRepository.calculateUserBalance(model.getUser().getId());
        if(balance.compareTo(model.getAmount())<0)
            throw new InsufficentBalanceException();
        var currentSubscription = subscriptionService.findByUserAndActivePackage(model.getUser().getId());
        if (currentSubscription == null)
            throw new InsufficentBalanceException();


        var totalDeposit = walletRepository.totalDeposit(model.getUser().getId());
        if (totalDeposit.compareTo(model.getAmount()) < 0)
            throw new InsufficentBalanceException();
        if (model.getAmount().compareTo(currentSubscription.getFinalPrice()) >= 0) {
            if (currentSubscription.getRemainingWithdrawalPerDay() > 0L)
                throw new NotAcceptableException(String.format("You can withdraw your funds after %d days.", currentSubscription.getRemainingWithdrawalPerDay()));

            SubscriptionPackageModel currentSubscriptionPackage = currentSubscription.getSubscriptionPackage();
            if (userService.countAllActiveChild(model.getUser().getId()) < currentSubscriptionPackage.getOrderCount()) {
                throw new NotAcceptableException(String.format("To withdraw your funds you need to have at least %d referrals.", currentSubscriptionPackage.getOrderCount()));
            }
        }
        model.setStatus(EntityStatusType.Pending);
    }
}
