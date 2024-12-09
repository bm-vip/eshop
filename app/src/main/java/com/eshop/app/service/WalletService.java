package com.eshop.app.service;

import com.eshop.app.enums.TransactionType;
import com.eshop.app.filter.WalletFilter;
import com.eshop.app.model.BalanceModel;
import com.eshop.app.model.WalletModel;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface WalletService extends BaseService<WalletFilter, WalletModel, Long> {
    List<BalanceModel> totalBalanceGroupedByCurrency();
    List<BalanceModel> totalDepositGroupedByCurrency();
    List<BalanceModel> totalWithdrawalGroupedByCurrency();
    List<BalanceModel> totalBonusGroupedByCurrency();
    List<BalanceModel> totalRewardGroupedByCurrency();
//    List<BalanceModel> totalProfitGroupedByCurrency();
    Map<Long, BigDecimal> findAllWithinDateRange(long startDate, long endDate, TransactionType transactionType);
}
