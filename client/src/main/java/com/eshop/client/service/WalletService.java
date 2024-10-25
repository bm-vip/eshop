package com.eshop.client.service;

import com.eshop.client.enums.TransactionType;
import com.eshop.client.filter.WalletFilter;
import com.eshop.client.model.BalanceModel;
import com.eshop.client.model.WalletModel;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface WalletService extends BaseService<WalletFilter, WalletModel, Long> {
    List<BalanceModel> totalBalanceGroupedByCurrency(long userId);
    List<BalanceModel> totalDepositGroupedByCurrency(long userId);
    List<BalanceModel> totalWithdrawalGroupedByCurrency(long userId);
    List<BalanceModel> totalBonusGroupedByCurrency(long userId);
    List<BalanceModel> totalProfitGroupedByCurrency(long userId);
    Map<Long, BigDecimal> findAllWithinDateRange(long startDate, long endDate, TransactionType transactionType);
}
