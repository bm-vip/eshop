package com.eshop.app.client.response;

import lombok.Data;

import java.util.List;

@Data
public class TokenInfoResponse {
    private List<TokenResult> result;
}