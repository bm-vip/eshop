package com.eshop.app.service.impl;

import com.eshop.app.entity.QSubscriptionPackageEntity;
import com.eshop.app.entity.SubscriptionPackageEntity;
import com.eshop.app.enums.CurrencyType;
import com.eshop.app.filter.SubscriptionPackageFilter;
import com.eshop.app.mapping.SubscriptionPackageMapper;
import com.eshop.app.model.SubscriptionPackageModel;
import com.eshop.app.repository.SubscriptionPackageRepository;
import com.eshop.app.service.SubscriptionPackageService;
import com.eshop.exception.common.NotFoundException;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class SubscriptionPackageServiceImpl extends BaseServiceImpl<SubscriptionPackageFilter,SubscriptionPackageModel, SubscriptionPackageEntity, Long> implements SubscriptionPackageService {

    private final SubscriptionPackageRepository subscriptionPackageRepository;
    private final JPAQueryFactory queryFactory;

    public SubscriptionPackageServiceImpl(SubscriptionPackageRepository repository, SubscriptionPackageMapper mapper, JPAQueryFactory queryFactory) {
        super(repository, mapper);
        this.subscriptionPackageRepository = repository;
        this.queryFactory = queryFactory;
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
        filter.getSelfReferralBonus().ifPresent(v -> builder.and(path.selfReferralBonus.eq(v)));
        filter.getSelfReferralBonusFrom().ifPresent(v -> builder.and(path.selfReferralBonus.goe(v)));
        filter.getSelfReferralBonusTo().ifPresent(v -> builder.and(path.selfReferralBonus.loe(v)));
        filter.getParentReferralBonus().ifPresent(v -> builder.and(path.parentReferralBonus.eq(v)));
        filter.getParentReferralBonusFrom().ifPresent(v -> builder.and(path.parentReferralBonus.goe(v)));
        filter.getParentReferralBonusTo().ifPresent(v -> builder.and(path.parentReferralBonus.loe(v)));

        return builder;
    }

    @Override
    public SubscriptionPackageModel findMatchedPackageByAmountAndCurrency(BigDecimal amount, CurrencyType currency) {
        return mapper.toModel(subscriptionPackageRepository.findMatchedPackageByAmountAndCurrency(amount, currency).orElse(subscriptionPackageRepository.findTopByOrderByMaxPriceDesc()));
    }
}
