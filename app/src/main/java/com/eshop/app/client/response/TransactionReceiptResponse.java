package com.eshop.app.client.response;

import lombok.Data;

@Data
public class TransactionReceiptResponse {
    private String jsonrpc;
    private int id;
    private TransactionReceiptResult result;
}
