package com.eshop.client.service.impl;

import com.eshop.client.entity.QWalletEntity;
import com.eshop.client.entity.WalletEntity;
import com.eshop.client.enums.EntityStatusType;
import com.eshop.client.enums.RoleType;
import com.eshop.client.filter.WalletFilter;
import com.eshop.client.mapping.WalletMapper;
import com.eshop.client.model.BalanceModel;
import com.eshop.client.model.SubscriptionModel;
import com.eshop.client.model.WalletModel;
import com.eshop.client.repository.WalletRepository;
import com.eshop.client.service.SubscriptionPackageService;
import com.eshop.client.service.SubscriptionService;
import com.eshop.client.service.WalletService;
import com.eshop.exception.common.NotFoundException;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WalletServiceImpl extends BaseServiceImpl<WalletFilter,WalletModel, WalletEntity, Long> implements WalletService {

    private final WalletRepository walletRepository;
    private final SubscriptionPackageService subscriptionPackageService;
    private final SubscriptionService subscriptionService;

    public WalletServiceImpl(WalletRepository repository, WalletMapper mapper, SubscriptionPackageService subscriptionPackageService, SubscriptionService subscriptionService) {
        super(repository, mapper);
        this.walletRepository = repository;
        this.subscriptionPackageService = subscriptionPackageService;
        this.subscriptionService = subscriptionService;
    }

    @Override
    public Predicate queryBuilder(WalletFilter filter) {
        BooleanBuilder builder = new BooleanBuilder();
        QWalletEntity path = QWalletEntity.walletEntity;

        if(!RoleType.hasRole(RoleType.ADMIN)) {
            builder.and(path.user.roles.any().role.ne(RoleType.ADMIN));
        }

        filter.getId().ifPresent(value -> builder.and(path.id.eq(value)));
        filter.getAmount().ifPresent(value -> builder.and(path.amount.eq(value)));
        filter.getAmountFrom().ifPresent(value -> builder.and(path.amount.goe(value)));
        filter.getAmountTo().ifPresent(value -> builder.and(path.amount.loe(value)));
        filter.getCurrency().ifPresent(value -> builder.and(path.currency.eq(value)));
        filter.getTransactionType().ifPresent(value -> builder.and(path.transactionType.eq(value)));
        filter.getTransactionHash().ifPresent(value -> builder.and(path.transactionHash.eq(value)));
        filter.getUserId().ifPresent(value -> builder.and(path.user.id.eq(value)));
        filter.getActive().ifPresent(value -> builder.and(path.active.eq(value)));
        filter.getAddress().ifPresent(value -> builder.and(path.address.eq(value)));

        return builder;
    }

    @Override
    @Transactional
    public WalletModel create(WalletModel model) {
        var result =  super.create(model);
        if(model.isActive()) {
            var balance = walletRepository.findBalanceGroupedByCurrency(model.getUser().getId());
            for (BalanceModel balanceModel : balance) {
                var currentSubscription = subscriptionService.findByUserAndActivePackage(model.getUser().getId());
                var subscriptionPackage = subscriptionPackageService.findMatchedPackageByAmountAndCurrency(balanceModel.getTotalAmount(),balanceModel.getCurrency());
                if(currentSubscription == null || (subscriptionPackage != null && !currentSubscription.getSubscriptionPackage().getId().equals(subscriptionPackage.getId()))) {
                    subscriptionService.create(new SubscriptionModel().setSubscriptionPackage(subscriptionPackage).setUser(model.getUser()).setStatus(EntityStatusType.Active));
                }
            }
        }
        return result;
    }

    @Override
    @Transactional
    public WalletModel update(WalletModel model) {
        var result =  super.update(model);
        if(model.isActive()) {
            var balance = walletRepository.findBalanceGroupedByCurrency(model.getUser().getId());
            var subscriptionModel = subscriptionService.findByUserAndActivePackage(model.getUser().getId());
            for (BalanceModel balanceModel : balance) {
                var subscriptionPackage = subscriptionPackageService.findMatchedPackageByAmountAndCurrency(balanceModel.getTotalAmount(),balanceModel.getCurrency());
                if(subscriptionPackage != null && !subscriptionModel.getSubscriptionPackage().getId().equals(subscriptionPackage.getId())) {
                    subscriptionService.create(new SubscriptionModel().setSubscriptionPackage(subscriptionPackage).setUser(model.getUser()).setStatus(EntityStatusType.Active));
                }
            }
        }
        return result;
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        WalletEntity entity = repository.findById(id).orElseThrow(() -> new NotFoundException("id: " + id));

        var balance = walletRepository.findBalanceGroupedByCurrency(entity.getUser().getId());
        var subscriptionModel = subscriptionService.findByUserAndActivePackage(entity.getUser().getId());
        for (BalanceModel balanceModel : balance) {
            if(subscriptionModel.getSubscriptionPackage().getCurrency().equals(balanceModel.getCurrency()) && subscriptionModel.getSubscriptionPackage().getPrice().compareTo(balanceModel.getTotalAmount()) > 0) {
                subscriptionService.logicalDeleteById(subscriptionModel.getId());
            }
        }

        repository.delete(entity);
    }
    
    @Override
    public List<BalanceModel> findBalanceGroupedByCurrency(long userId) {
        return walletRepository.findBalanceGroupedByCurrency(userId);
    }
    @Override
    public List<BalanceModel> findDepositGroupedByCurrency(long userId) {
        return walletRepository.findDepositGroupedByCurrency(userId);
    }
    @Override
    public List<BalanceModel> findWithdrawalGroupedByCurrency(long userId) {
        return walletRepository.findWithdrawalGroupedByCurrency(userId);
    }
    @Override
    public List<BalanceModel> findBonusGroupedByCurrency(long userId) {
        return walletRepository.findBonusGroupedByCurrency(userId);
    }
}
