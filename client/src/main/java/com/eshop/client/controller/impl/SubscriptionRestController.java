package com.eshop.client.controller.impl;

import com.eshop.client.controller.LogicalDeletedRestController;
import com.eshop.client.filter.SubscriptionFilter;
import com.eshop.client.model.SubscriptionModel;
import com.eshop.client.service.LogicalDeletedService;
import com.eshop.client.service.SubscriptionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Subscription Rest Service v1")
@RequestMapping(value = "/api/v1/subscription")
public class SubscriptionRestController extends BaseRestControllerImpl<SubscriptionFilter, SubscriptionModel, Long> implements LogicalDeletedRestController<Long> {

    private SubscriptionService subscriptionService;

    public SubscriptionRestController(SubscriptionService service) {
        super(service, SubscriptionFilter.class);
        this.subscriptionService = service;
    }

    @Override
    public LogicalDeletedService<Long> getService() {
        return subscriptionService;
    }

    @GetMapping("find-active-by-user/{userId}")
    public ResponseEntity<SubscriptionModel> findAll(@PathVariable Long userId){
        return ResponseEntity.ok(subscriptionService.findByUserAndActivePackage(userId));
    }
}
