package com.eshop.client.controller.impl;


import com.eshop.client.controller.BaseRestController;
import com.eshop.client.model.BaseModel;
import com.eshop.client.model.PageModel;
import com.eshop.client.model.Select2Model;
import com.eshop.client.service.BaseService;
import com.eshop.exception.common.BadRequestException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;

@Slf4j
@PropertySource("classpath:i18n/messages.properties")
public abstract class BaseRestControllerImpl<F,M extends BaseModel<ID>, ID extends Serializable> implements BaseRestController<F, M, ID> {
    protected BaseService<F, M, ID> service;
    protected Class<F> clazz;
    protected ObjectMapper objectMapper;

    public BaseRestControllerImpl(BaseService<F, M, ID> service, Class<F> clazz) {
        this.clazz = clazz;
        this.service = service;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public ResponseEntity<M> findById(ID id) {
        log.debug("call BaseRestImpl.findById {}, for class {}", id, clazz.getName());
        return ResponseEntity.ok(service.findById(id));
    }

    @SneakyThrows
    @Override
    public ResponseEntity<Page<M>> findAll(F filter, Pageable pageable) {
        log.debug("call BaseRestImpl.findAll {}, for class {}", filter, clazz.getName());
        return ResponseEntity.ok(service.findAll(filter, pageable));
    }

    @SneakyThrows
    @Override
    public ResponseEntity<PageModel> findAllTable(F filter, Pageable pageable) {
        log.debug("call BaseRestImpl.findAllTable {}, for class {}", filter, clazz.getName());
        return ResponseEntity.ok(service.findAllTable(filter, pageable));
    }

    @SneakyThrows
    @Override
    public Page<Select2Model> findAllSelect(F filter, int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.Direction.DESC, "id");
        return service.findAllSelect(filter, pageable);
    }

    @SneakyThrows
    @Override
    public ResponseEntity<Long> countAll(F filter) {
        log.debug("call BaseRestImpl.countAll {}, for class {}", filter, clazz.getName());
        return ResponseEntity.ok(service.countAll(filter));
    }

    @SneakyThrows
    @Override
    public ResponseEntity<Boolean> exists(F filter) {
        log.debug("call BaseRestImpl.exists {}, for class {}", filter, clazz.getName());
        return ResponseEntity.ok(service.exists(filter));
    }

    @Override
    public ResponseEntity<Void> deleteById(ID id) {
        log.debug("call BaseRestImpl.deleteById {}, for class {}", id, clazz.getName());
        service.deleteById(id);
        return ResponseEntity.noContent().build();

    }

    @Override
    public ResponseEntity<M> create(M model) {
        log.debug("call BaseRestImpl.save for {}", model);
        if (model.getId() != null) {
            throw new BadRequestException("The id must not be provided when creating a new record");
        }
        return ResponseEntity.ok(service.create(model));
    }

    @Override
    public ResponseEntity<M> update(M model) {
        log.debug("call BaseRestImpl.update for {}", model);
        return ResponseEntity.ok(service.update(model));
    }
}
