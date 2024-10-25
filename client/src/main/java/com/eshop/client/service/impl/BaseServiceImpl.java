package com.eshop.client.service.impl;

import com.eshop.client.model.Select2Model;
import com.eshop.client.repository.BaseRepository;
import com.eshop.client.service.BaseService;
import com.eshop.exception.common.NotFoundException;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import com.eshop.client.entity.BaseEntity;
import com.eshop.client.mapping.BaseMapper;
import com.eshop.client.model.BaseModel;
import com.eshop.client.model.PageModel;

import java.io.Serializable;

@RequiredArgsConstructor
public abstract class BaseServiceImpl<F, M extends BaseModel<ID>, E extends BaseEntity<ID>, ID extends Serializable> implements BaseService<F, M, ID> {

    public final BaseRepository<E, ID> repository;
    public final BaseMapper<M, E> mapper;

    public abstract Predicate queryBuilder(F filter);

    @Override
    @Transactional(readOnly = true)
    public Page<M> findAll(F filter, Pageable pageable) {
        return repository.findAll(queryBuilder(filter), pageable).map(mapper::toModel);
    }

    @Override
    @Transactional(readOnly = true)
    public PageModel findAllTable(F filter, Pageable pageable) {
        Predicate predicate = queryBuilder(filter);
        var page = repository.findAll(predicate, pageable);

        return new PageModel(repository.count(), page.getTotalElements(), mapper.toModel(page.getContent()));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Select2Model> findAllSelect(F filter, Pageable pageable) {
        Predicate predicate = queryBuilder(filter);
        return repository.findAll(predicate, pageable).map(m -> new Select2Model(m.getId().toString(), m.getSelectTitle()));
    }

    @Override
    @Transactional(readOnly = true)
    public Long countAll(F filter) {
        return repository.count(queryBuilder(filter));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(F filter) {
        return repository.exists(queryBuilder(filter));
    }

    @Override
    @Transactional
    public M findById(ID id) {
        var entity = repository.findById(id).orElseThrow(() -> new NotFoundException("id: " + id));
        return mapper.toModel(entity);
    }

    @Override
    public M create(M model) {
        return mapper.toModel(repository.save(mapper.toEntity(model)));
    }

    @Override
    public M update(M model) {
        var entity = repository.findById(model.getId()).orElseThrow(() -> new NotFoundException(String.format("%s not found by id %d", model.getClass().getName(), model.getId().toString())));
        return mapper.toModel(repository.save(mapper.updateEntity(model, entity)));
    }

    @Override
    public void deleteById(ID id) {
        var entity = repository.findById(id).orElseThrow(() -> new NotFoundException("id: " + id));
        repository.delete(entity);
    }
}
