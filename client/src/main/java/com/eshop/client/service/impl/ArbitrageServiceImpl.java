package com.eshop.client.service.impl;

import com.eshop.client.entity.ArbitrageEntity;
import com.eshop.client.entity.QArbitrageEntity;
import com.eshop.client.filter.ArbitrageFilter;
import com.eshop.client.mapping.ArbitrageMapper;
import com.eshop.client.model.ArbitrageModel;
import com.eshop.client.repository.ArbitrageRepository;
import com.eshop.client.service.ArbitrageService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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
        filter.getId().ifPresent(v->builder.and(p.id.eq(v)));
        filter.getCreatedDateFrom().ifPresent(v-> builder.and(p.createdDate.goe(v)));
        filter.getCreatedDateTo().ifPresent(v-> builder.and(p.createdDate.loe(v)));
        filter.getUserId().ifPresent(v-> builder.and(p.user.id.eq(v)));
        filter.getExchangeId().ifPresent(v-> builder.and(p.exchange.id.eq(v)));
        filter.getCoinId().ifPresent(v-> builder.and(p.coin.id.eq(v)));
        filter.getSubscriptionId().ifPresent(v-> builder.and(p.subscription.id.eq(v)));

        return builder;
    }

    @Override
    public long countAllByUserId(long userId) {
        return repository.countAllByUserId(userId);
    }
}
