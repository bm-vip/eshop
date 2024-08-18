package com.eshop.app.service;

import com.eshop.app.filter.SubscriptionFilter;
import com.eshop.app.model.SubscriptionModel;

public interface SubscriptionService extends BaseService<SubscriptionFilter, SubscriptionModel, Long>, LogicalDeletedService<Long>{
    SubscriptionModel findByUserAndActivePackage(long userId);
}
