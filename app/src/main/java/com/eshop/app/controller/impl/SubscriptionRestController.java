package com.eshop.app.controller.impl;

import com.eshop.app.controller.LogicalDeletedRestController;
import com.eshop.app.filter.SubscriptionFilter;
import com.eshop.app.model.SubscriptionModel;
import com.eshop.app.service.LogicalDeletedService;
import com.eshop.app.service.SubscriptionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Subscription Rest Service v1")
@RequestMapping(value = "/api/v1/subscription")
public  class SubscriptionRestController extends BaseRestControllerImpl<SubscriptionFilter, SubscriptionModel, Long> implements LogicalDeletedRestController<Long> {

    private SubscriptionService subscriptionService;

    public SubscriptionRestController(SubscriptionService service) {
        super(service);
        this.subscriptionService = service;
    }


    @Override
    public LogicalDeletedService<Long> getService() {
        return subscriptionService;
    }
}
