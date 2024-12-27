package com.eshop.app.service;

import com.eshop.app.enums.CurrencyType;
import com.eshop.app.filter.SubscriptionPackageFilter;
import com.eshop.app.model.SubscriptionPackageModel;

import java.math.BigDecimal;

public interface SubscriptionPackageService extends BaseService<SubscriptionPackageFilter, SubscriptionPackageModel, Long> {
    SubscriptionPackageModel findMatchedPackageByAmount(BigDecimal amount);
}
