package com.eshop.client.strategy.impl;

import com.eshop.client.enums.CurrencyType;
import com.eshop.client.enums.EntityStatusType;
import com.eshop.client.model.WalletModel;
import com.eshop.client.repository.WalletRepository;
import com.eshop.client.service.ParameterService;
import com.eshop.client.service.SubscriptionService;
import com.eshop.client.service.UserService;
import com.eshop.client.strategy.TransactionStrategy;
import com.eshop.exception.common.InsufficentBalanceException;
import com.eshop.exception.common.NotAcceptableException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class WithdrawalStrategyImpl implements TransactionStrategy {
    private final SubscriptionService subscriptionService;
    private final WalletRepository walletRepository;
    private final UserService userService;
    private final String minWithdrawAmount;
    private final String subUserPercentage;
    private final String userPercentage;

    public WithdrawalStrategyImpl(SubscriptionService subscriptionService, WalletRepository walletRepository, UserService userService, ParameterService parameterService) {
        this.subscriptionService = subscriptionService;
        this.walletRepository = walletRepository;
        this.userService = userService;
        this.minWithdrawAmount = parameterService.findByCode("MIN_WITHDRAW").getValue();
        this.subUserPercentage = parameterService.findByCode("SUB_USER_PERCENTAGE").getValue();
        this.userPercentage = parameterService.findByCode("USER_PERCENTAGE").getValue();
    }

    @Override
    public void execute(WalletModel model) {
        model.setActualAmount(model.getAmount());
        model.setCurrency(CurrencyType.USDT);
        var currentSubscription = subscriptionService.findByUserAndActivePackage(model.getUser().getId());
        if (currentSubscription == null)
            throw new InsufficentBalanceException();

        var totalBalance = walletRepository.totalBalance(model.getUser().getId());
        if (totalBalance.compareTo(model.getAmount()) < 0)
            throw new InsufficentBalanceException();

//        if (model.getAmount().compareTo(currentSubscription.getFinalPrice()) >= 0) {
//            if (currentSubscription.getRemainingWithdrawalPerDay() > 0L)
//                throw new NotAcceptableException(String.format("You can withdraw your funds after %d days.", currentSubscription.getRemainingWithdrawalPerDay()));
//
//            SubscriptionPackageModel currentSubscriptionPackage = currentSubscription.getSubscriptionPackage();
//            if (userService.countAllActiveChild(model.getUser().getId()) < currentSubscriptionPackage.getOrderCount()) {
//                throw new NotAcceptableException(String.format("To withdraw your funds you need to have at least %d referrals.", currentSubscriptionPackage.getOrderCount()));
//            }
//        }
        synchronized (model.getUser().getId().toString().intern()) {
            BigDecimal totalDepositOfSubUsersPercentage = walletRepository.totalBalanceOfSubUsers(model.getUser().getId()).multiply(new BigDecimal(subUserPercentage));
            BigDecimal totalDepositOfMyPercentage = totalBalance.multiply(new BigDecimal(userPercentage));
            BigDecimal allowedWithdrawal = totalDepositOfMyPercentage.add(totalDepositOfSubUsersPercentage);
            if (allowedWithdrawal.compareTo(BigDecimal.ZERO) < 0)
                allowedWithdrawal = BigDecimal.ZERO;
            else if (allowedWithdrawal.compareTo(totalBalance) > 0)
                allowedWithdrawal = totalBalance;

            if (allowedWithdrawal.compareTo(model.getAmount()) < 0) {
                throw new NotAcceptableException("""
                            You can withdraw your deposited amount up to : <strong>%d USD</strong>.<br/>
                            To increase your withdrawal amount, please bring more referrals!"""
                        .formatted(allowedWithdrawal.longValue()));
            }
        }
        model.setStatus(EntityStatusType.Pending);
    }
}
