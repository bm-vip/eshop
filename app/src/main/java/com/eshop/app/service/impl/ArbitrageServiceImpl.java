package com.eshop.app.service.impl;

import com.eshop.app.entity.ArbitrageEntity;
import com.eshop.app.entity.QArbitrageEntity;
import com.eshop.app.enums.RoleType;
import com.eshop.app.filter.ArbitrageFilter;
import com.eshop.app.mapping.ArbitrageMapper;
import com.eshop.app.model.ArbitrageModel;
import com.eshop.app.repository.ArbitrageRepository;
import com.eshop.app.service.ArbitrageService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class ArbitrageServiceImpl extends BaseServiceImpl<ArbitrageFilter, ArbitrageModel, ArbitrageEntity, Long> implements ArbitrageService {

    private ArbitrageRepository repository;
    private ArbitrageMapper mapper;

    @Autowired
    public ArbitrageServiceImpl(ArbitrageRepository repository, ArbitrageMapper mapper) {
        super(repository, mapper);
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Predicate queryBuilder(ArbitrageFilter filter) {
        QArbitrageEntity p = QArbitrageEntity.arbitrageEntity;
        BooleanBuilder builder = new BooleanBuilder();
        if(!RoleType.hasRole(RoleType.ADMIN)) {
            builder.and(p.role.eq(RoleType.firstRole()));
        }
        filter.getId().ifPresent(v->builder.and(p.id.eq(v)));
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
    public long countAllByUserId(UUID userId) {
        return repository.countAllByUserId(userId);
    }
}
