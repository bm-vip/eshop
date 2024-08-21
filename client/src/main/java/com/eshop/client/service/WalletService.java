package com.eshop.client.service;

import com.eshop.client.filter.WalletFilter;
import com.eshop.client.model.BalanceModel;
import com.eshop.client.model.WalletModel;

import java.util.List;

public interface WalletService extends BaseService<WalletFilter, WalletModel, Long> {
    List<BalanceModel> findBalanceGroupedByCurrency(long userId);
    List<BalanceModel> findDepositGroupedByCurrency(long userId);
    List<BalanceModel> findWithdrawalGroupedByCurrency(long userId);
    List<BalanceModel> findBonusGroupedByCurrency(long userId);
}
