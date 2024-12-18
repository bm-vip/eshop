package com.eshop.app.controller.impl;

import com.eshop.app.filter.ExchangeFilter;
import com.eshop.app.model.ExchangeModel;
import com.eshop.app.service.ExchangeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Exchange Rest Service v1")
@RequestMapping(value = "/api/v1/exchange")
public class ExchangeRestController extends BaseRestControllerImpl<ExchangeFilter, ExchangeModel, Long>  {

    private ExchangeService exchangeService;

    public ExchangeRestController(ExchangeService service) {
        super(service);
        this.exchangeService = service;
    }
}
