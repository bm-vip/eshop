package com.eshop.app.dto;

import lombok.Data;

@Data
public class BsTxResponse {
    private String jsonrpc;
    private int id;
    private TransactionResult result;
}
