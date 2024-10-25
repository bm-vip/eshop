package com.eshop.client.service.impl;

import com.eshop.client.entity.ArbitrageEntity;
import com.eshop.client.entity.QArbitrageEntity;
import com.eshop.client.entity.UserEntity;
import com.eshop.client.entity.WalletEntity;
import com.eshop.client.enums.EntityStatusType;
import com.eshop.client.enums.TransactionType;
import com.eshop.client.filter.ArbitrageFilter;
import com.eshop.client.mapping.ArbitrageMapper;
import com.eshop.client.model.ArbitrageModel;
import com.eshop.client.repository.ArbitrageRepository;
import com.eshop.client.repository.SubscriptionPackageRepository;
import com.eshop.client.repository.SubscriptionRepository;
import com.eshop.client.repository.WalletRepository;
import com.eshop.client.service.ArbitrageService;
import com.eshop.client.service.ParameterService;
import com.eshop.client.util.DateUtil;
import com.eshop.exception.common.NotAcceptableException;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.DateTemplate;
import com.querydsl.core.types.dsl.Expressions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;


@Service
public class ArbitrageServiceImpl extends BaseServiceImpl<ArbitrageFilter, ArbitrageModel, ArbitrageEntity, Long> implements ArbitrageService {

    private ArbitrageRepository repository;
    private ArbitrageMapper mapper;
    private final SubscriptionPackageRepository subscriptionPackageRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final ParameterService parameterService;
    private final WalletRepository walletRepository;
    private final String walletAddressValue;

    @Autowired
    public ArbitrageServiceImpl(ArbitrageRepository repository, ArbitrageMapper mapper, SubscriptionPackageRepository subscriptionPackageRepository, SubscriptionRepository subscriptionRepository, ParameterService parameterService, WalletRepository walletRepository) {
        super(repository, mapper);
        this.repository = repository;
        this.mapper = mapper;
        this.subscriptionPackageRepository = subscriptionPackageRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.parameterService = parameterService;
        this.walletAddressValue = parameterService.findByCode(ParameterService.walletAddress).getValue();
        this.walletRepository = walletRepository;
    }

    @Override
    public Predicate queryBuilder(ArbitrageFilter filter) {
        QArbitrageEntity p = QArbitrageEntity.arbitrageEntity;
        BooleanBuilder builder = new BooleanBuilder();
        DateTemplate<Date> truncatedDate = Expressions.dateTemplate(Date.class, "date_trunc('day', {0})", p.createdDate);

        filter.getId().ifPresent(v->builder.and(p.id.eq(v)));
        filter.getCreatedDate().ifPresent(v-> builder.and(truncatedDate.eq(v)));
        filter.getCreatedDateFrom().ifPresent(v-> builder.and(p.createdDate.goe(v)));
        filter.getCreatedDateTo().ifPresent(v-> builder.and(p.createdDate.loe(v)));
        filter.getUserId().ifPresent(v-> builder.and(p.user.id.eq(v)));
        filter.getExchangeId().ifPresent(v-> builder.and(p.exchange.id.eq(v)));
        filter.getCoinId().ifPresent(v-> builder.and(p.coin.id.eq(v)));
        filter.getSubscriptionId().ifPresent(v-> builder.and(p.subscription.id.eq(v)));
        filter.getRewardFrom().ifPresent(v-> builder.and(p.reward.goe(v)));
        filter.getRewardTo().ifPresent(v-> builder.and(p.reward.loe(v)));
        filter.getCurrency().ifPresent(v-> builder.and(p.currency.eq(v)));

        return builder;
    }

    @Override
    @Transactional
    public ArbitrageModel create(ArbitrageModel model) {
        if(dailyLimitPurchase(model.getUser().getId()))
            throw new NotAcceptableException("You have reached the daily purchase limitation, please try tomorrow.");

        var subscription = subscriptionRepository.findByUserIdAndStatus(model.getUser().getId(), EntityStatusType.Active);
        var balanceList = walletRepository.totalBalanceGroupedByCurrency(model.getUser().getId());
        var balance = balanceList.stream().filter(x->x.getCurrency().equals(subscription.getSubscriptionPackage().getCurrency())).findFirst();
        var user = new UserEntity();user.setId(model.getUser().getId());
        var reward = subscription.getSubscriptionPackage().getReward(balance.get().getTotalAmount());

        if(reward.compareTo(BigDecimal.ZERO) == 1) {
            WalletEntity buyReward = new WalletEntity();
            buyReward.setActive(true);
            buyReward.setAmount(reward);
            buyReward.setUser(user);
            buyReward.setCurrency(subscription.getSubscriptionPackage().getCurrency());
            buyReward.setTransactionType(TransactionType.REWARD);
            buyReward.setAddress(walletAddressValue);
            walletRepository.save(buyReward);
            model.setReward(buyReward.getAmount());
            model.setCurrency(subscription.getSubscriptionPackage().getCurrency());

            return super.create(model);
        }
        throw new NotAcceptableException("Your profit is equal to zero, please contact support.");
    }

    @Override
    public long countAllByUserId(long userId) {
        return repository.countAllByUserId(userId);
    }
    @Override
    @Transactional(readOnly = true)
    public boolean dailyLimitPurchase(long userId){
        var subscription = subscriptionRepository.findByUserIdAndStatus(userId, EntityStatusType.Active);
        return countAll(new ArbitrageFilter().setCreatedDate(DateUtil.truncate(new Date())).setSubscriptionId(subscription.getId()).setUserId(userId)) >= subscription.getSubscriptionPackage().getOrderCount();
    }
}
