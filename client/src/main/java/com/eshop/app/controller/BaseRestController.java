package com.eshop.app.controller;

import com.eshop.app.model.PageModel;
import com.eshop.app.model.Select2Model;
import com.eshop.app.validation.Save;
import com.eshop.app.validation.Update;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;

@Validated
public interface BaseRestController<F, M, ID extends Serializable> {

    @GetMapping(value = {"/{id}"})
    @Operation(summary = "${api.baseRest.findById}", description = "${api.baseRest.findById.desc}")
    ResponseEntity<M> findById(@PathVariable("id") ID id);

    @GetMapping
    @Operation(summary = "${api.baseRest.findAll}", description = "${api.baseRest.findAll.desc}")
    ResponseEntity<Page<M>> findAll(F filter, @PageableDefault Pageable pageable);

    @GetMapping(value = {"/findAllTable"})
    @Operation(summary = "${api.baseRest.findAllTable}", description = "${api.baseRest.findAll.desc}")
    ResponseEntity<PageModel> findAllTable(F filter, @PageableDefault Pageable pageable);

    @GetMapping(value = {"/findAllSelect"})
    @Operation(summary = "${api.baseRest.findAllSelect}", description = "${api.baseRest.findAll.desc}")
    Page<Select2Model> findAllSelect(F filter, @RequestParam int page);

    @GetMapping(value = {"/countAll"})
    @Operation(summary = "${api.baseRest.countAll}", description = "${api.baseRest.countAll.desc}")
    ResponseEntity<Long> countAll(F filter);

    @GetMapping(value = {"/exists"})
    @Operation(summary = "${api.baseRest.exists}", description = "${api.baseRest.exists.desc}")
    ResponseEntity<Boolean> exists(F filter);

    @DeleteMapping(value = {"/{id}"})
    @Operation(summary = "${api.baseRest.deleteById}", description = "${api.baseRest.deleteById.desc}")
    ResponseEntity<Void> deleteById(@PathVariable("id") ID id);

    @PostMapping
    @ResponseBody
    @Operation(summary = "${api.baseRest.save}", description = "${api.baseRest.save.desc}")
    ResponseEntity<M> create(@Validated(Save.class) @RequestBody M model);

    @PatchMapping
    @ResponseBody
    @Operation(summary = "${api.baseRest.update}", description = "${api.baseRest.update.desc}")
    ResponseEntity<M> update(@Validated(Update.class) @RequestBody M model);
}
