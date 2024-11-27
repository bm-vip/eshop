package com.eshop.app.service;

import com.eshop.app.filter.SubscriptionFilter;
import com.eshop.app.model.SubscriptionModel;

import java.util.UUID;

public interface SubscriptionService extends BaseService<SubscriptionFilter, SubscriptionModel, Long>, LogicalDeletedService<Long>{
    SubscriptionModel findByUserAndActivePackage(UUID userId);
}
