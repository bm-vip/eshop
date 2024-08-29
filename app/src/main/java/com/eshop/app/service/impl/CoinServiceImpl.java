package com.eshop.app.service.impl;

import com.eshop.app.entity.CoinEntity;
import com.eshop.app.entity.QCoinEntity;
import com.eshop.app.filter.CoinFilter;
import com.eshop.app.mapping.CoinMapper;
import com.eshop.app.model.CoinModel;
import com.eshop.app.repository.CoinRepository;
import com.eshop.app.service.CoinService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CoinServiceImpl extends BaseServiceImpl<CoinFilter, CoinModel, CoinEntity, Long> implements CoinService {

    private CoinRepository repository;
    private CoinMapper mapper;

    @Autowired
    public CoinServiceImpl(CoinRepository repository, CoinMapper mapper) {
        super(repository, mapper);
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Predicate queryBuilder(CoinFilter filter) {
        QCoinEntity p = QCoinEntity.coinEntity;
        BooleanBuilder builder = new BooleanBuilder();
        filter.getId().ifPresent(v->builder.and(p.id.eq(v)));
        filter.getName().ifPresent(v->builder.and(p.name.toLowerCase().contains(v.toLowerCase())));
        filter.getLogo().ifPresent(v->builder.and(p.logo.eq(v)));

        return builder;
    }
}
