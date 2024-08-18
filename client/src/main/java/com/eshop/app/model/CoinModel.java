package com.eshop.app.model;

import lombok.Data;

@Data
public class CoinModel extends BaseModel<Long> {
    private String name;
    private String logo;
}
