package com.eshop.client.service;

import com.eshop.client.filter.ArbitrageFilter;
import com.eshop.client.model.ArbitrageModel;
import com.eshop.client.model.CoinUsageDTO;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.Date;

public interface ArbitrageService extends BaseService<ArbitrageFilter, ArbitrageModel, Long> {
    long countAllByUserId(long userId);
    long countAllByUserIdAndDate(long userId, Date date);
    String purchaseLimit(long userId);
    Page<CoinUsageDTO> findMostUsedCoins(int pageSize);
}
