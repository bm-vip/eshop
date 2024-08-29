package com.eshop.app.service;

import com.eshop.app.filter.ArbitrageFilter;
import com.eshop.app.model.ArbitrageModel;

public interface ArbitrageService extends BaseService<ArbitrageFilter, ArbitrageModel, Long> {
    long countAllByUserId(long userId);
}
