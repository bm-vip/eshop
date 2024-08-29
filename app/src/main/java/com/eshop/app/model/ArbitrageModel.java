package com.eshop.app.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ArbitrageModel extends BaseModel<Long> {
    private UserModel user;
    private ExchangeModel exchange;
    private CoinModel coin;
    private SubscriptionModel subscription;

}
