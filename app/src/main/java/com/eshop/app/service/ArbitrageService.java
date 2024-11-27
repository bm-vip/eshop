package com.eshop.app.service;

import com.eshop.app.filter.ArbitrageFilter;
import com.eshop.app.model.ArbitrageModel;

import java.util.UUID;

public interface ArbitrageService extends BaseService<ArbitrageFilter, ArbitrageModel, Long> {
    long countAllByUserId(UUID userId);
}
