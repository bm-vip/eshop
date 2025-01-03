package com.eshop.client.strategy.impl;

import com.eshop.client.enums.CurrencyType;
import com.eshop.client.enums.EntityStatusType;
import com.eshop.client.model.WalletModel;
import com.eshop.client.repository.WalletRepository;
import com.eshop.client.service.ParameterService;
import com.eshop.client.service.SubscriptionService;
import com.eshop.client.strategy.TransactionStrategy;
import com.eshop.exception.common.InsufficentBalanceException;
import com.eshop.exception.common.NotAcceptableException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class WithdrawalProfitStrategyImpl implements TransactionStrategy {
    private final SubscriptionService subscriptionService;
    private final WalletRepository walletRepository;
    private final String minWithdrawAmount;
    private final String subUserPercentage;
    private final String userPercentage;

    public WithdrawalProfitStrategyImpl(SubscriptionService subscriptionService, WalletRepository walletRepository, ParameterService parameterService) {
        this.subscriptionService = subscriptionService;
        this.walletRepository = walletRepository;
        this.minWithdrawAmount = parameterService.findByCode("MIN_WITHDRAW").getValue();
        this.subUserPercentage = parameterService.findByCode("SUB_USER_PERCENTAGE").getValue();
        this.userPercentage = parameterService.findByCode("USER_PERCENTAGE").getValue();
    }

    @Override
    public void execute(WalletModel model) {
        model.setActualAmount(model.getAmount());
        model.setCurrency(CurrencyType.USDT);
        var totalProfit = walletRepository.totalProfit(model.getUser().getId());
        if(totalProfit.compareTo(model.getAmount())<0)
            throw new InsufficentBalanceException();

        var currentSubscription = subscriptionService.findByUserAndActivePackage(model.getUser().getId());
        if (currentSubscription == null)
            throw new InsufficentBalanceException();

        if (model.getAmount().compareTo(new BigDecimal(minWithdrawAmount)) < 0)
            throw new InsufficentBalanceException(String.format("Your requested amount %s should greater than %s", model.getAmount().toString(), minWithdrawAmount));
        synchronized (model.getUser().getId().toString().intern()) {
            BigDecimal totalDepositOfSubUsersPercentage = walletRepository.totalBalanceOfSubUsers(model.getUser().getId()).multiply(new BigDecimal(subUserPercentage));
            BigDecimal totalDepositOfMyPercentage = walletRepository.totalBalance(model.getUser().getId()).multiply(new BigDecimal(userPercentage));
            BigDecimal allowedWithdrawal = totalDepositOfMyPercentage.add(totalDepositOfSubUsersPercentage);
            if (allowedWithdrawal.compareTo(BigDecimal.ZERO) < 0)
                allowedWithdrawal = BigDecimal.ZERO;
            else if (allowedWithdrawal.compareTo(totalProfit) > 0)
                allowedWithdrawal = totalProfit;
            if (allowedWithdrawal.compareTo(model.getAmount()) < 0) {
                throw new NotAcceptableException("""
                            You can withdraw your profit amount up to : <strong>%d USD</strong>.<br/>
                            To increase your withdrawal amount, please bring more referrals!"""
                        .formatted(allowedWithdrawal.longValue()));
            }
        }
        model.setStatus(EntityStatusType.Pending);
    }
}
