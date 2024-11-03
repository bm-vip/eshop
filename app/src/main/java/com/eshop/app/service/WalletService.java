package com.eshop.app.service;

import com.eshop.app.filter.WalletFilter;
import com.eshop.app.model.BalanceModel;
import com.eshop.app.model.WalletModel;

import java.util.List;

public interface WalletService extends BaseService<WalletFilter, WalletModel, Long> {
    List<BalanceModel> totalBalanceGroupedByCurrency(long userId);
    List<BalanceModel> totalDepositGroupedByCurrency(long userId);
    List<BalanceModel> totalWithdrawalGroupedByCurrency(long userId);
    List<BalanceModel> totalBonusGroupedByCurrency(long userId);
    List<BalanceModel> totalRewardGroupedByCurrency(long userId);
    List<BalanceModel> totalProfitGroupedByCurrency(long userId);
}
