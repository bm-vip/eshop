package com.eshop.app.model;

import com.eshop.app.enums.CurrencyType;
import com.eshop.app.validation.Create;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class ArbitrageModel extends BaseModel<Long> {
    @NotNull(groups = Create.class)
    private UserModel user;
    @NotNull(groups = Create.class)
    private ExchangeModel exchange;
    @NotNull(groups = Create.class)
    private CoinModel coin;
    @NotNull(groups = Create.class)
    private SubscriptionModel subscription;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal reward;
    private CurrencyType currency;
}
