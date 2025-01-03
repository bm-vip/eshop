package com.eshop.app.strategy.impl;

import com.eshop.app.enums.CurrencyType;
import com.eshop.app.enums.EntityStatusType;
import com.eshop.app.enums.RoleType;
import com.eshop.app.model.SubscriptionModel;
import com.eshop.app.model.WalletModel;
import com.eshop.app.repository.WalletRepository;
import com.eshop.app.service.ParameterService;
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
public class WithdrawalProfitStrategyImpl implements TransactionStrategy {
    private final WalletRepository walletRepository;
    private final NetworkStrategyFactory networkStrategyFactory;
    private final SubscriptionService subscriptionService;
    private final SubscriptionPackageService subscriptionPackageService;
    private final String minWithdrawAmount;
    private final String subUserPercentage;
    private final String userPercentage;

    public WithdrawalProfitStrategyImpl(WalletRepository walletRepository, NetworkStrategyFactory networkStrategyFactory, SubscriptionService subscriptionService, SubscriptionPackageService subscriptionPackageService, ParameterService parameterService) {
        this.walletRepository = walletRepository;
        this.networkStrategyFactory = networkStrategyFactory;
        this.subscriptionService = subscriptionService;
        this.subscriptionPackageService = subscriptionPackageService;
        this.minWithdrawAmount = parameterService.findByCode("MIN_WITHDRAW").getValue();
        this.subUserPercentage = parameterService.findByCode("SUB_USER_PERCENTAGE").getValue();
        this.userPercentage = parameterService.findByCode("USER_PERCENTAGE").getValue();
    }

    @Override
    public void beforeSave(WalletModel model) {
        model.setActualAmount(model.getAmount());
        model.setCurrency(CurrencyType.USDT);
        var totalProfit = walletRepository.totalProfitByUserId(model.getUser().getId());
        if (totalProfit.compareTo(model.getAmount()) < 0)
            throw new InsufficentBalanceException();

        if (model.getAmount().compareTo(new BigDecimal(minWithdrawAmount)) < 0)
            throw new InsufficentBalanceException(String.format("Your requested amount %s should greater than %s", model.getAmount().toString(), minWithdrawAmount));

        var network = networkStrategyFactory.get(model.getNetwork());
        if(model.getStatus().equals(EntityStatusType.Active) && (RoleType.hasRole(RoleType.ADMIN) || network.validate(model))) {
            synchronized (model.getUser().getId().toString().intern()) {
                BigDecimal totalDepositOfSubUsersPercentage = walletRepository.totalBalanceOfSubUsers(model.getUser().getId()).multiply(new BigDecimal(subUserPercentage));
                BigDecimal totalDepositOfMinePercentage = walletRepository.totalBalanceByUserId(model.getUser().getId()).multiply(new BigDecimal(userPercentage));
                BigDecimal allowedWithdrawal = totalDepositOfMinePercentage.add(totalDepositOfSubUsersPercentage);
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
