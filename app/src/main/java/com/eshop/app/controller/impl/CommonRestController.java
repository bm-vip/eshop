package com.eshop.app.controller.impl;

import com.eshop.app.model.Select2Model;
import com.eshop.app.util.ReflectionUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@Tag(name = "Common Rest Service v1")
@RequestMapping(value = "/api/v1/common")
public class CommonRestController {

    @GetMapping("/getEnum/{name}")
    public ResponseEntity<List<Select2Model>> getEnum(@PathVariable String name) {
        List<Select2Model> list = (List<Select2Model>) ReflectionUtil.invokeMethod("com.eshop.app.enums.".concat(name), "getAll");
        return ResponseEntity.ok(list);
    }
    @GetMapping(value = {"/get-html-file/{name}"})
    public ModelAndView getHtml(@PathVariable String name, @RequestParam("params") String json) throws IOException {
        Map<String, String> map = new ObjectMapper().readValue(json, Map.class);
        ModelAndView modelAndView = new ModelAndView();
        map.forEach((k, v) -> {
            modelAndView.addObject(k, v);
        });
        modelAndView.setViewName(name);
        return modelAndView;
    }
}
