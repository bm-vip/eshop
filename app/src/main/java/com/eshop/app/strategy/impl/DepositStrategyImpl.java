package com.eshop.app.strategy.impl;


import com.eshop.app.entity.WalletEntity;
import com.eshop.app.enums.EntityStatusType;
import com.eshop.app.enums.RoleType;
import com.eshop.app.enums.TransactionType;
import com.eshop.app.model.SubscriptionModel;
import com.eshop.app.model.WalletModel;
import com.eshop.app.repository.UserRepository;
import com.eshop.app.repository.WalletRepository;
import com.eshop.app.service.SubscriptionPackageService;
import com.eshop.app.service.SubscriptionService;
import com.eshop.app.strategy.NetworkStrategyFactory;
import com.eshop.app.strategy.TransactionStrategy;
import com.eshop.exception.common.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static com.eshop.app.util.MapperHelper.get;

@Service
@RequiredArgsConstructor
public class DepositStrategyImpl implements TransactionStrategy {
    private final WalletRepository walletRepository;
    private final UserRepository userRepository;
    private final SubscriptionService subscriptionService;
    private final SubscriptionPackageService subscriptionPackageService;
    private final NetworkStrategyFactory networkStrategyFactory;

    @Override
    public void beforeSave(WalletModel model) {

    }

    @Override
    public void afterSave(WalletModel model) {
        var network = networkStrategyFactory.get(model.getNetwork());
        if (model.getStatus().equals(EntityStatusType.Active) && (RoleType.hasRole(RoleType.ADMIN) || network.validate(model))) {
            var balance = walletRepository.calculateUserBalance(model.getUser().getId());
            var currentSubscription = subscriptionService.findByUserAndActivePackage(model.getUser().getId());

            var nextSubscriptionPackage = subscriptionPackageService.findMatchedPackageByAmount(balance);
            if (nextSubscriptionPackage != null && (currentSubscription == null || !currentSubscription.getSubscriptionPackage().getId().equals(nextSubscriptionPackage.getId()))) {
                subscriptionService.create(new SubscriptionModel().setSubscriptionPackage(nextSubscriptionPackage).setUser(model.getUser()).setStatus(EntityStatusType.Active));
            }
            var user = userRepository.findById(model.getUser().getId()).orElseThrow(()->new NotFoundException("user not found"));
            if (walletRepository.countByUserIdAndTransactionTypeAndStatus(model.getUser().getId(), TransactionType.DEPOSIT, EntityStatusType.Active) == 1) {
                if (get(() -> user.getParent()) != null) {
                    WalletEntity bonus1 = new WalletEntity();
                    bonus1.setStatus(EntityStatusType.Active);
                    bonus1.setUser(user.getParent());
                    bonus1.setAmount(referralDepositBonus(model.getAmount()));
                    bonus1.setActualAmount(referralDepositBonus(model.getAmount()));
                    bonus1.setTransactionType(TransactionType.BONUS);
                    bonus1.setRole(user.getRole());
                    walletRepository.save(bonus1);
                }
            }
        }
    }
    public BigDecimal referralDepositBonus(BigDecimal amount) {
//        List<ParameterModel> parameters = parameterService.findAllByParameterGroupCode("REFERRAL_DEPOSIT_BONUS");
//        for (ParameterModel parameter : parameters) {
//            // Check if the amount falls within the range
//            var split = parameter.getTitle().split("~");
//            BigDecimal lowerLimit = new BigDecimal(split[0]);
//            BigDecimal upperLimit = new BigDecimal(split[1]);
//            if (amount.compareTo(lowerLimit) >= 0 && amount.compareTo(upperLimit) <= 0) {
//                return new BigDecimal(parameter.getValue());
//            }
//        }
        return BigDecimal.ONE;
    }
}
