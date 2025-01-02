package com.eshop.client.dto;

import lombok.Data;

@Data
public class TransactionReceiptResponse {
    private String jsonrpc;
    private int id;
    private TransactionReceiptResult result;
}
