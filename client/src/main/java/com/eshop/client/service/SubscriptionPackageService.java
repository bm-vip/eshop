package com.eshop.client.service;

import com.eshop.client.enums.CurrencyType;
import com.eshop.client.filter.SubscriptionPackageFilter;
import com.eshop.client.model.SubscriptionPackageModel;

import java.math.BigDecimal;

public interface SubscriptionPackageService extends BaseService<SubscriptionPackageFilter, SubscriptionPackageModel, Long> {
    SubscriptionPackageModel findMatchedPackageByAmountAndCurrency(BigDecimal amount, CurrencyType currency);
}
