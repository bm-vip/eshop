package com.eshop.app.controller.impl;

import com.eshop.app.model.Select2Model;
import com.eshop.app.util.ReflectionUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "Common Rest Service v1")
@RequestMapping(value = "/api/v1/common")
public class CommonRestController {

    @GetMapping("/getEnum/{name}")
    public ResponseEntity<List<Select2Model>> getEnum(@PathVariable String name) {
        List<Select2Model> list = (List<Select2Model>) ReflectionUtil.invokeMethod("com.eshop.app.enums.".concat(name), "getAll");
        return ResponseEntity.ok(list);
    }
}
