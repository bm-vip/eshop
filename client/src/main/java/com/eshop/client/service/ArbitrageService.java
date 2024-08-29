package com.eshop.client.service;

import com.eshop.client.filter.ArbitrageFilter;
import com.eshop.client.model.ArbitrageModel;

public interface ArbitrageService extends BaseService<ArbitrageFilter, ArbitrageModel, Long> {
    long countAllByUserId(long userId);
}
