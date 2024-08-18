package com.eshop.app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.eshop.app.service.LogicalDeletedService;

import java.io.Serializable;


public interface LogicalDeletedRestController<ID extends Serializable> {

    LogicalDeletedService<ID> getService();

    @DeleteMapping(value = {"/logical-deleteById/{id}"})
    default ResponseEntity<Void> logicalDeleteById(@PathVariable("id") ID id) {
        getService().logicalDeleteById(id);
        return ResponseEntity.noContent().build();
    }
}
