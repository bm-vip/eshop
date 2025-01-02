package com.eshop.app.strategy;

import com.eshop.app.model.WalletModel;

public interface TransactionStrategy {
    void beforeSave(WalletModel model);
    void afterSave(WalletModel model);
}
