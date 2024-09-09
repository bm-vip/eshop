package com.eshop.client.service;

import com.eshop.client.filter.SubscriptionFilter;
import com.eshop.client.model.SubscriptionModel;

import java.math.BigDecimal;

public interface SubscriptionService extends BaseService<SubscriptionFilter, SubscriptionModel, Long>, LogicalDeletedService<Long>{
    SubscriptionModel findByUserAndActivePackage(long userId);
}
