package com.eshop.app.service.impl;

import com.eshop.app.entity.ParameterGroupEntity;
import com.eshop.app.entity.QParameterGroupEntity;
import com.eshop.app.filter.ParameterGroupFilter;
import com.eshop.app.mapping.ParameterGroupMapper;
import com.eshop.app.model.ParameterGroupModel;
import com.eshop.app.repository.ParameterGroupRepository;
import com.eshop.app.service.ParameterGroupService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;


@Service
public class ParameterGroupServiceImpl extends BaseServiceImpl<ParameterGroupFilter, ParameterGroupModel, ParameterGroupEntity, Long> implements ParameterGroupService {

    private ParameterGroupRepository repository;
    private ParameterGroupMapper mapper;

    @Autowired
    public ParameterGroupServiceImpl(ParameterGroupRepository repository, ParameterGroupMapper mapper) {
        super(repository, mapper);
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Predicate queryBuilder(ParameterGroupFilter filter) {
        QParameterGroupEntity p = QParameterGroupEntity.parameterGroupEntity;
        BooleanBuilder builder = new BooleanBuilder();
        filter.getId().ifPresent(v->builder.and(p.id.eq(v)));
        filter.getCode().ifPresent(v->builder.and(p.code.eq(v)));
        filter.getTitle().ifPresent(v->builder.and(p.title.eq(v)));

        return builder;
    }

    public ParameterGroupEntity findByCode(String code) {
        return repository.findByCode(code);
    }

    @Override
    public JpaRepository<ParameterGroupEntity,Long> getRepository() {
        return repository;
    }
}
