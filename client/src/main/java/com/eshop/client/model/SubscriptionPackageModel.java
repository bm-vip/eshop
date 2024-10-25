package com.eshop.client.model;

import com.eshop.client.enums.CurrencyType;
import com.eshop.client.enums.EntityStatusType;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
public class SubscriptionPackageModel extends BaseModel<Long> {
    @NotEmpty
    private String name;
    private Integer duration;//days
    @NotNull
    private Integer orderCount;
    @NotNull
    private BigDecimal price;
    private BigDecimal maxPrice;
    @NotNull
    private CurrencyType currency;
    private String description;
    private EntityStatusType status;
    private Float minTradingReward;
    private Float maxTradingReward;
    private Float parentReferralBonus;
    private int withdrawalDurationPerDay;
    private int userProfitPercentage;
    private int siteProfitPercentage;
}
