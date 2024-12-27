package com.eshop.app;

import com.eshop.app.enums.EntityStatusType;
import com.eshop.app.enums.TransactionType;
import com.eshop.app.mapping.WalletMapper;
import com.eshop.app.model.SubscriptionModel;
import com.eshop.app.model.UserModel;
import com.eshop.app.model.WalletModel;
import com.eshop.app.repository.WalletRepository;
import com.eshop.app.service.NotificationService;
import com.eshop.app.service.SubscriptionPackageService;
import com.eshop.app.service.SubscriptionService;
import com.eshop.app.service.WalletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static com.eshop.app.util.MapperHelper.get;

@Component
@RequiredArgsConstructor
@Slf4j
public class Scheduler {
    private final WalletRepository walletRepository;
    private final WalletService walletService;
    private final SubscriptionService subscriptionService;
    private final SubscriptionPackageService subscriptionPackageService;
    private final NotificationService notificationService;
    private final WalletMapper walletMapper;

    @Scheduled(cron = "0 */3 * * * *")
    public void validateTransactions() {
        log.info("Wallet Scheduler has started!");
        walletRepository.findAllByStatusAndTransactionHashIsNotNullAndTransactionType(EntityStatusType.Pending, TransactionType.DEPOSIT).forEach(we -> {
            WalletModel model = walletMapper.toModel(we);
            boolean transactionIsValid = walletService.validateTransaction(model);
            if (transactionIsValid) {
                model.setStatus(EntityStatusType.Active);
                we.setStatus(EntityStatusType.Active);
                var balance = walletRepository.calculateUserBalance(model.getUser().getId());
                var currentSubscription = subscriptionService.findByUserAndActivePackage(model.getUser().getId());

                var subscriptionPackage = subscriptionPackageService.findMatchedPackageByAmount(balance);
                if (subscriptionPackage != null && (currentSubscription == null || !currentSubscription.getSubscriptionPackage().getId().equals(subscriptionPackage.getId()))) {
                    currentSubscription = subscriptionService.create(new SubscriptionModel().setSubscriptionPackage(subscriptionPackage).setUser(model.getUser()).setStatus(EntityStatusType.Active));
                }

                if (walletRepository.countByUserIdAndTransactionTypeAndStatus(model.getUser().getId(), TransactionType.DEPOSIT, EntityStatusType.Active) == 1) {
                    var user = we.getUser();
                    if (get(() -> user.getParent()) != null) {
                        WalletModel bonus1 = new WalletModel();
                        bonus1.setStatus(EntityStatusType.Active);
                        bonus1.setUser(new UserModel().setUserId(user.getParent().getId()));
                        bonus1.setAmount(walletService.referralDepositBonus(model.getAmount()));
                        bonus1.setTransactionType(TransactionType.BONUS);
                        bonus1.setRole(user.getRole());
                        walletService.create(bonus1);
                    }
                }
                notificationService.sendTransactionNotification(walletMapper.toModel(we));
            } else {
                we.setStatus(EntityStatusType.Invalid);
            }
            walletRepository.save(we);
        });
    }
}
