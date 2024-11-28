package com.eshop.client.service.impl;

import com.eshop.client.entity.QSubscriptionPackageEntity;
import com.eshop.client.entity.SubscriptionPackageEntity;
import com.eshop.client.enums.CurrencyType;
import com.eshop.client.filter.SubscriptionPackageFilter;
import com.eshop.client.mapping.SubscriptionPackageMapper;
import com.eshop.client.model.SubscriptionPackageModel;
import com.eshop.client.repository.SubscriptionPackageRepository;
import com.eshop.client.service.SubscriptionPackageService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class SubscriptionPackageServiceImpl extends BaseServiceImpl<SubscriptionPackageFilter,SubscriptionPackageModel, SubscriptionPackageEntity, Long> implements SubscriptionPackageService {

    private final SubscriptionPackageRepository subscriptionPackageRepository;

    public SubscriptionPackageServiceImpl(SubscriptionPackageRepository repository, SubscriptionPackageMapper mapper) {
        super(repository, mapper);
        this.subscriptionPackageRepository = repository;
    }

    @Override
    public Predicate queryBuilder(SubscriptionPackageFilter filter) {
        BooleanBuilder builder = new BooleanBuilder();
        QSubscriptionPackageEntity path = QSubscriptionPackageEntity.subscriptionPackageEntity;

        filter.getId().ifPresent(v -> builder.and(path.id.eq(v)));
        filter.getName().ifPresent(v -> builder.and(path.name.toLowerCase().contains(v.toLowerCase())));
        filter.getDuration().ifPresent(v -> builder.and(path.duration.eq(v)));
        filter.getOrderCount().ifPresent(v -> builder.and(path.orderCount.eq(v)));
        filter.getPrice().ifPresent(v -> builder.and(path.price.eq(v)));
        filter.getPriceFrom().ifPresent(v -> builder.and(path.price.goe(v)));
        filter.getPriceTo().ifPresent(v -> builder.and(path.price.loe(v)));
        filter.getMaxPrice().ifPresent(v -> builder.and(path.maxPrice.eq(v)));
        filter.getMaxPriceFrom().ifPresent(v -> builder.and(path.maxPrice.goe(v)));
        filter.getMaxPriceTo().ifPresent(v -> builder.and(path.maxPrice.loe(v)));
        filter.getCurrency().ifPresent(v -> builder.and(path.currency.eq(v)));
        filter.getStatus().ifPresent(v -> builder.and(path.status.eq(v)));
        filter.getDescription().ifPresent(v -> builder.and(path.description.toLowerCase().contains(v.toLowerCase())));
        filter.getMinTradingReward().ifPresent(v -> builder.and(path.minTradingReward.eq(v)));
        filter.getMinTradingRewardFrom().ifPresent(v -> builder.and(path.minTradingReward.goe(v)));
        filter.getMinTradingRewardTo().ifPresent(v -> builder.and(path.minTradingReward.loe(v)));
        filter.getMaxTradingReward().ifPresent(v -> builder.and(path.maxTradingReward.eq(v)));
        filter.getMaxTradingRewardFrom().ifPresent(v -> builder.and(path.maxTradingReward.goe(v)));
        filter.getMaxTradingRewardTo().ifPresent(v -> builder.and(path.maxTradingReward.loe(v)));
        filter.getParentReferralBonus().ifPresent(v -> builder.and(path.parentReferralBonus.eq(v)));
        filter.getParentReferralBonusFrom().ifPresent(v -> builder.and(path.parentReferralBonus.goe(v)));
        filter.getParentReferralBonusTo().ifPresent(v -> builder.and(path.parentReferralBonus.loe(v)));
        filter.getWithdrawalDurationPerDay().ifPresent(v -> builder.and(path.withdrawalDurationPerDay.eq(v)));
        filter.getUserProfitPercentage().ifPresent(v -> builder.and(path.userProfitPercentage.eq(v)));
        filter.getSiteProfitPercentage().ifPresent(v -> builder.and(path.siteProfitPercentage.eq(v)));

        return builder;
    }

    @Override
    @Cacheable(cacheNames = "${cache.prefix:client}", key = "'SubscriptionPackage:findMatchedPackageByAmountAndCurrency:amount:' + #amount.toString() + ':currency:' + #currency.toString()")
    public SubscriptionPackageModel findMatchedPackageByAmountAndCurrency(BigDecimal amount, CurrencyType currency) {
        return mapper.toModel(subscriptionPackageRepository.findMatchedPackageByAmountAndCurrency(amount, currency).orElse(subscriptionPackageRepository.findTopByOrderByMaxPriceDesc()));
    }
}
