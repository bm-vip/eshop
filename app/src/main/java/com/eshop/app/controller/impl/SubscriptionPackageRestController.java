package com.eshop.app.controller.impl;

import com.eshop.app.filter.SubscriptionPackageFilter;
import com.eshop.app.model.SubscriptionPackageModel;
import com.eshop.app.service.SubscriptionPackageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Subscription Package Rest Service v1")
@RequestMapping(value = "/api/v1/subscription-package")
public class SubscriptionPackageRestController extends BaseRestControllerImpl<SubscriptionPackageFilter, SubscriptionPackageModel, Long> {

    private SubscriptionPackageService subscriptionPackageService;

    public SubscriptionPackageRestController(SubscriptionPackageService service) {
        super(service);
        this.subscriptionPackageService = service;
    }
}
