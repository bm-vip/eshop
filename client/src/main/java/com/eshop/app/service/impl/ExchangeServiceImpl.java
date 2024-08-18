package com.eshop.app.service.impl;

import com.eshop.app.entity.ExchangeEntity;
import com.eshop.app.entity.QExchangeEntity;
import com.eshop.app.filter.ExchangeFilter;
import com.eshop.app.mapping.ExchangeMapper;
import com.eshop.app.model.ExchangeModel;
import com.eshop.app.repository.ExchangeRepository;
import com.eshop.app.service.ExchangeService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ExchangeServiceImpl extends BaseServiceImpl<ExchangeFilter, ExchangeModel, ExchangeEntity, Long> implements ExchangeService {

    private ExchangeRepository repository;
    private ExchangeMapper mapper;

    @Autowired
    public ExchangeServiceImpl(ExchangeRepository repository, ExchangeMapper mapper) {
        super(repository, mapper);
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Predicate queryBuilder(ExchangeFilter filter) {
        QExchangeEntity p = QExchangeEntity.exchangeEntity;
        BooleanBuilder builder = new BooleanBuilder();
        filter.getId().ifPresent(v->builder.and(p.id.eq(v)));
        filter.getName().ifPresent(v->builder.and(p.name.eq(v)));
        filter.getLogo().ifPresent(v->builder.and(p.logo.eq(v)));

        return builder;
    }
}
