package com.eshop.app.model;

import com.eshop.app.enums.CurrencyType;
import com.eshop.app.enums.EntityStatusType;
import com.eshop.app.enums.TransactionType;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
