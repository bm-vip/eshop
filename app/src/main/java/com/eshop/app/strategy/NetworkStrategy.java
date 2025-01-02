package com.eshop.app.strategy;

import com.eshop.app.dto.TransactionResponse;
import com.eshop.app.model.WalletModel;

import java.math.BigDecimal;
import java.math.RoundingMode;

public interface NetworkStrategy {
    BigDecimal getPrice(String token);
    TransactionResponse getTransactionInfo(String hash);
    default boolean validate(WalletModel model) {
        try {
            var transaction = getTransactionInfo(model.getTransactionHash());
            if (transaction == null)
                return false;
            if (!transaction.isSuccess())
                return false;
            if (transaction.getAmount().subtract(model.getActualAmount().setScale(4, RoundingMode.HALF_UP)).abs().compareTo(new BigDecimal("0.0001")) != 0)
                return false;
            if (!transaction.getToAddress().equals(model.getAddress()))
                return false;
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
