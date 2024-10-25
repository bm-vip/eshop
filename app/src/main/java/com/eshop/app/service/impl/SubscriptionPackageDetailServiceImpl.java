package com.eshop.app.service.impl;

import com.eshop.app.entity.QSubscriptionPackageDetailEntity;
import com.eshop.app.entity.SubscriptionPackageDetailEntity;
import com.eshop.app.filter.SubscriptionPackageDetailFilter;
import com.eshop.app.mapping.SubscriptionPackageDetailMapper;
import com.eshop.app.model.SubscriptionPackageDetailModel;
import com.eshop.app.repository.SubscriptionPackageDetailRepository;
import com.eshop.app.repository.SubscriptionPackageRepository;
import com.eshop.app.service.SubscriptionPackageDetailService;
import com.eshop.exception.common.BadRequestException;
import com.eshop.exception.common.NotFoundException;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionPackageDetailServiceImpl extends BaseServiceImpl<SubscriptionPackageDetailFilter, SubscriptionPackageDetailModel, SubscriptionPackageDetailEntity, Long> implements SubscriptionPackageDetailService {

    private final SubscriptionPackageDetailRepository repository;
    private final SubscriptionPackageRepository subscriptionPackageRepository;

    public SubscriptionPackageDetailServiceImpl(SubscriptionPackageDetailRepository repository, SubscriptionPackageDetailMapper mapper, SubscriptionPackageRepository subscriptionPackageRepository) {
        super(repository, mapper);
        this.repository = repository;
        this.subscriptionPackageRepository = subscriptionPackageRepository;
    }

    @Override
    public Predicate queryBuilder(SubscriptionPackageDetailFilter filter) {
        BooleanBuilder builder = new BooleanBuilder();
        QSubscriptionPackageDetailEntity path = QSubscriptionPackageDetailEntity.subscriptionPackageDetailEntity;

        filter.getId().ifPresent(v -> builder.and(path.id.eq(v)));
        filter.getSubscriptionPackageId().ifPresent(v -> builder.and(path.subscriptionPackage.id.eq(v)));
        filter.getAmountFrom().ifPresent(v -> builder.and(path.amount.goe(v)));
        filter.getAmountTo().ifPresent(v -> builder.and(path.amount.loe(v)));
        filter.getMinProfit().ifPresent(v -> builder.and(path.minProfit.eq(v)));
        filter.getMaxProfit().ifPresent(v -> builder.and(path.maxProfit.eq(v)));

        return builder;
    }

    @Override
    public SubscriptionPackageDetailModel create(SubscriptionPackageDetailModel model) {
        var subscriptionPackage = subscriptionPackageRepository.findById(model.getSubscriptionPackage().getId()).orElseThrow(() -> new NotFoundException(String.format("%s not found by id %d", model.getClass().getName(), model.getSubscriptionPackage().getId().toString())));
        if(model.getMinProfit().floatValue() < subscriptionPackage.getMinTradingReward())
            throw new BadRequestException("MinProfit must be greater or equal than " + subscriptionPackage.getMinTradingReward());
        if(model.getMinProfit().floatValue() > subscriptionPackage.getMaxTradingReward())
            throw new BadRequestException("MinProfit must be less or equal than " + subscriptionPackage.getMaxTradingReward());
        if(model.getMaxProfit().floatValue() < subscriptionPackage.getMinTradingReward())
            throw new BadRequestException("MaxProfit must be greater or equal than " + subscriptionPackage.getMinTradingReward());
        if(model.getMaxProfit().floatValue() > subscriptionPackage.getMaxTradingReward())
            throw new BadRequestException("MaxProfit must be less or equal than " + subscriptionPackage.getMaxTradingReward());
        return super.create(model);
    }

    @Override
    public Page<SubscriptionPackageDetailModel> findBySubscriptionPackageId(long subscriptionPackageId, Pageable pageable) {
        return repository.findBySubscriptionPackageId(subscriptionPackageId, pageable).map(mapper::toModel);
    }
}
