package com.eshop.client.client;

import com.eshop.client.client.response.TransactionResponse;

import java.math.BigDecimal;

public interface NetworkStrategy {
    BigDecimal getPrice(String token);
    TransactionResponse getTransactionInfo(String hash);
}
