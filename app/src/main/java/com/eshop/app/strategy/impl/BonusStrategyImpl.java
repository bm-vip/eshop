package com.eshop.app.strategy.impl;


import com.eshop.app.model.WalletModel;
import com.eshop.app.strategy.TransactionStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BonusStrategyImpl implements TransactionStrategy {

    @Override
    public void beforeSave(WalletModel model) {

    }

    @Override
    public void afterSave(WalletModel model) {

    }
}
