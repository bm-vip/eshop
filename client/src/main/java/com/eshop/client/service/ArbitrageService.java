package com.eshop.client.service;

import com.eshop.client.filter.ArbitrageFilter;
import com.eshop.client.model.ArbitrageModel;

import java.time.LocalDateTime;

public interface ArbitrageService extends BaseService<ArbitrageFilter, ArbitrageModel, Long> {
    long countAllByUserId(long userId);
    LocalDateTime dailyLimitPurchase(long userId);
}
