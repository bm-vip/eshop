package com.eshop.client.strategy.impl;

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

    public WithdrawalProfitStrategyImpl(SubscriptionService subscriptionService, WalletRepository walletRepository, ParameterService parameterService) {
        this.subscriptionService = subscriptionService;
        this.walletRepository = walletRepository;
        this.minWithdrawAmount = parameterService.findByCode("MIN_WITHDRAW").getValue();
    }

    @Override
    public void execute(WalletModel model) {
        var totalProfit = walletRepository.totalProfit(model.getUser().getId());
        if(totalProfit.compareTo(model.getAmount())<0)
            throw new InsufficentBalanceException();

        var currentSubscription = subscriptionService.findByUserAndActivePackage(model.getUser().getId());
        if (currentSubscription == null)
            throw new InsufficentBalanceException();

        if (model.getAmount().compareTo(new BigDecimal(minWithdrawAmount)) < 0)
            throw new InsufficentBalanceException(String.format("Your requested amount %s should greater than %s", model.getAmount().toString(), minWithdrawAmount));

        BigDecimal totalDeposit = walletRepository.totalDeposit(model.getUser().getId()).multiply(BigDecimal.valueOf(0.3));
        BigDecimal totalWithdrawalProfit = walletRepository.totalWithdrawalProfit(model.getUser().getId());
        BigDecimal allowedWithdrawal = totalDeposit.subtract(totalWithdrawalProfit);
        if (allowedWithdrawal.compareTo(model.getAmount()) < 0)
            throw new NotAcceptableException(String.format("You can withdraw profit up to 30%% of your deposited amount (%d USD).<br/>Your total withdrawal profit is %d USD.<br/> Your allowed withdrawal profit is %d USD.",
                    totalDeposit.longValue(), totalWithdrawalProfit.longValue(), allowedWithdrawal.longValue()));

        model.setStatus(EntityStatusType.Pending);
    }
}
