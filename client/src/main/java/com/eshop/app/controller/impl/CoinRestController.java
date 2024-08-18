package com.eshop.app.controller.impl;

import com.eshop.app.filter.CoinFilter;
import com.eshop.app.model.CoinModel;
import com.eshop.app.service.CoinService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Coin Rest Service v1")
@RequestMapping(value = "/api/v1/coin")
public class CoinRestController extends BaseRestControllerImpl<CoinFilter, CoinModel, Long>  {

    private CoinService coinService;

    public CoinRestController(CoinService service) {
        super(service, CoinFilter.class);
        this.coinService = service;
    }
}
