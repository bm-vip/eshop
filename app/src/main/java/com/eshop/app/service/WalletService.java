package com.eshop.app.service;

import com.eshop.app.filter.WalletFilter;
import com.eshop.app.model.BalanceModel;
import com.eshop.app.model.WalletModel;

import java.util.List;
import java.util.UUID;

public interface WalletService extends BaseService<WalletFilter, WalletModel, Long> {
    List<BalanceModel> totalBalanceGroupedByCurrency(UUID userId);
    List<BalanceModel> totalDepositGroupedByCurrency(UUID userId);
    List<BalanceModel> totalWithdrawalGroupedByCurrency(UUID userId);
    List<BalanceModel> totalBonusGroupedByCurrency(UUID userId);
    List<BalanceModel> totalRewardGroupedByCurrency(UUID userId);
//    List<BalanceModel> totalProfitGroupedByCurrency(UUID userId);
}
