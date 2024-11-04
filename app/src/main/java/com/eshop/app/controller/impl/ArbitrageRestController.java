package com.eshop.app.controller.impl;

import com.eshop.app.filter.ArbitrageFilter;
import com.eshop.app.model.ArbitrageModel;
import com.eshop.app.service.ArbitrageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Arbitrage Rest Service v1")
@RequestMapping(value = "/api/v1/arbitrage")
public class ArbitrageRestController extends BaseRestControllerImpl<ArbitrageFilter, ArbitrageModel, Long>  {

    private ArbitrageService arbitrageService;

    public ArbitrageRestController(ArbitrageService service) {
        super(service);
        this.arbitrageService = service;
    }
    @GetMapping("count-all-by-user/{userId}")
    public long countAllByUserId(@PathVariable long userId) {
        return arbitrageService.countAllByUserId(userId);
    }
}
