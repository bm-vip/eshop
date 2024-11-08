package com.eshop.app.service.impl;

import com.eshop.app.entity.BaseEntity;
import com.eshop.app.mapping.BaseMapper;
import com.eshop.app.model.BaseModel;
import com.eshop.app.model.PageModel;
import com.eshop.app.model.Select2Model;
import com.eshop.app.repository.BaseRepository;
import com.eshop.app.service.BaseService;
import com.eshop.exception.common.NotFoundException;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
@RequiredArgsConstructor
public abstract class BaseServiceImpl<F, M extends BaseModel<ID>, E extends BaseEntity<ID>, ID extends Serializable> implements BaseService<F, M, ID> {

    protected final BaseRepository<E, ID> repository;
    protected final BaseMapper<M, E> mapper;
    @PersistenceContext
    protected EntityManager entityManager;

    public abstract Predicate queryBuilder(F filter);

    @Override
    @Transactional(readOnly = true)
    public Page<M> findAll(F filter, Pageable pageable) {
        return repository.findAll(queryBuilder(filter), pageable).map(mapper::toModel);
    }

    @Override
    @Transactional(readOnly = true)
    public PageModel<M> findAllTable(F filter, Pageable pageable) {
        Predicate predicate = queryBuilder(filter);
        Page<E> page = repository.findAll(predicate, pageable);

        return new PageModel<>(repository.count(), page.getTotalElements(), mapper.toModel(page.getContent()));
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
    @Transactional(readOnly = true)
    public M findById(ID id) {
        E entity = repository.findById(id).orElseThrow(() -> new NotFoundException("id: " + id));
        return mapper.toModel(entity);
    }

    @Override
    public M create(M model) {
        return mapper.toModel(repository.save(mapper.toEntity(model)));
    }
    @Override
    public M update(M model) {
        E entity = repository.findById(model.getId()).orElseThrow(() -> new NotFoundException(String.format("%s not found by id %d", model.getClass().getName(), model.getId().toString())));
        return mapper.toModel(repository.save(mapper.updateEntity(model, entity)));
    }

    @Override
    public void deleteById(ID id) {
        E entity = repository.findById(id).orElseThrow(() -> new NotFoundException("id: " + id));
        repository.delete(entity);
    }
}
