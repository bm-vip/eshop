package com.eshop.app.service.impl;

import com.eshop.app.entity.ParameterEntity;
import com.eshop.app.entity.QParameterEntity;
import com.eshop.app.filter.ParameterFilter;
import com.eshop.app.filter.ParameterGroupFilter;
import com.eshop.app.mapping.ParameterMapper;
import com.eshop.app.model.ParameterModel;
import com.eshop.app.repository.ParameterRepository;
import com.eshop.app.service.ParameterService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class ParameterServiceImpl extends BaseServiceImpl<ParameterFilter,ParameterModel, ParameterEntity, Long> implements ParameterService {
    private ParameterRepository repository;
    private ParameterMapper mapper;

    @Autowired
    public ParameterServiceImpl(ParameterRepository repository, ParameterMapper mapper) {
        super(repository, mapper);
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Predicate queryBuilder(ParameterFilter filter) {
        QParameterEntity p = QParameterEntity.parameterEntity;
        BooleanBuilder builder = new BooleanBuilder();
        filter.getId().ifPresent(v -> builder.and(p.id.eq(v)));
        filter.getCode().ifPresent(v -> builder.and(p.code.eq(v)));
        filter.getTitle().ifPresent(v -> builder.and(p.title.contains(v)));
        filter.getValue().ifPresent(v -> builder.and(p.value.contains(v)));
        filter.getParameterGroup().ifPresent(v -> v.getId().ifPresent(id -> builder.and(p.parameterGroup.id.eq(id))));
        filter.getParameterGroup().ifPresent(v -> v.getCode().ifPresent(code -> builder.and(p.parameterGroup.code.eq(code))));

        return builder;
    }

    public ParameterModel findByCode(String code) {
        Optional<ParameterEntity> optional = repository.findByCode(code);
        if (optional.isPresent())
            return mapper.toModel(optional.get());
        return new ParameterModel();
    }
    public List<ParameterModel> findAllByParameterGroupCode(String parameterGroupCode) {
        ParameterFilter filter = new ParameterFilter(){{setParameterGroup(new ParameterGroupFilter(){{setCode(parameterGroupCode);}});}};
        return super.findAll(filter, PageRequest.ofSize(1000)).getContent();
    }

    @Override
    public JpaRepository<ParameterEntity,Long> getRepository() {
        return repository;
    }
}
