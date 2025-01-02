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
    WalletModel createFromAdmin(WalletModel model);
    BigDecimal totalBalance();
    BigDecimal totalDeposit();
    BigDecimal totalWithdrawal();
    BigDecimal totalBonus();
    BigDecimal totalReward();
//    List<BalanceModel> totalProfit();
    Map<Long, BigDecimal> findAllWithinDateRange(long startDate, long endDate, TransactionType transactionType);
    BigDecimal referralDepositBonus(BigDecimal amount);
}
