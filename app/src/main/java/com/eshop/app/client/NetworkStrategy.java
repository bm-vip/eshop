package com.eshop.app.client;

import com.eshop.app.client.response.TransactionResponse;

import java.math.BigDecimal;

public interface NetworkStrategy {
    BigDecimal getPrice(String token);
    TransactionResponse getTransactionInfo(String hash);
}
