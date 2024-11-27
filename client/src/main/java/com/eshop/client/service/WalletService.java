package com.eshop.client.service;

import com.eshop.client.enums.TransactionType;
import com.eshop.client.filter.WalletFilter;
import com.eshop.client.model.BalanceModel;
import com.eshop.client.model.WalletModel;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface WalletService extends BaseService<WalletFilter, WalletModel, Long> {
    List<BalanceModel> totalBalanceGroupedByCurrency(UUID userId);
    List<BalanceModel> totalDepositGroupedByCurrency(UUID userId);
    List<BalanceModel> totalWithdrawalGroupedByCurrency(UUID userId);
    List<BalanceModel> totalBonusGroupedByCurrency(UUID userId);
    List<BalanceModel> totalRewardGroupedByCurrency(UUID userId);
    List<BalanceModel> totalProfitGroupedByCurrency(UUID userId);
    List<BalanceModel> dailyProfitGroupedByCurrency(UUID userId);
    Map<Long, BigDecimal> findAllWithinDateRange(long startDate, long endDate, TransactionType transactionType);
}
