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
    private Float selfReferralBonus;
    private Float parentReferralBonus;

    public BigDecimal getReward(BigDecimal balance) {
        if (balance.compareTo(price) < 0 || balance.compareTo(maxPrice) > 0) {
//            throw new IllegalArgumentException("User balance must be within the range of price and maxPrice");
            return BigDecimal.ZERO;
        }
        // Normalize user balance within the range [0, 1]
        BigDecimal balanceRange = maxPrice.subtract(price);
        BigDecimal normalizedBalance = balance.subtract(price).divide(balanceRange, 10, RoundingMode.HALF_UP);
        // Scale the reward based on the normalized balance
        var _minTradingReward = new BigDecimal(Float.toString(minTradingReward));
        var _maxTradingReward = new BigDecimal(Float.toString(maxTradingReward));
        BigDecimal rewardRange = _maxTradingReward.subtract(_minTradingReward);
        BigDecimal reward = _minTradingReward.add(normalizedBalance.multiply(rewardRange));

        return reward.setScale(2, RoundingMode.HALF_UP); // Set the scale to 2 decimal places
    }
}
