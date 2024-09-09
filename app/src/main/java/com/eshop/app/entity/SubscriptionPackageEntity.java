package com.eshop.app.entity;

import com.eshop.app.enums.CurrencyType;
import com.eshop.app.enums.EntityStatusType;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
@Entity
@Table(name = "tbl_subscription_package")
public class SubscriptionPackageEntity extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_subscription_package")
    @SequenceGenerator(name = "seq_subscription_package", sequenceName = "seq_subscription_package", allocationSize = 1, initialValue = 1)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(nullable = false)
    private Integer duration;//days
    @Column(name = "order_count")
    private Integer orderCount;
    @Column(nullable = false)
    private BigDecimal price;
    @Column(name = "max_price",nullable = false)
    private BigDecimal maxPrice;
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private CurrencyType currency;
    @Column(nullable = false)
    private String description;
    @Column(name = "status", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private EntityStatusType status;
    @Column(name = "min_trading_reward", nullable = false)
    private Float minTradingReward;
    @Column(name = "max_trading_reward", nullable = false)
    private Float maxTradingReward;
    @Column(name = "self_referral_bonus", nullable = false)
    private Float selfReferralBonus;
    @Column(name = "parent_referral_bonus", nullable = false)
    private Float parentReferralBonus;

    @Override
    public String getSelectTitle() {
        if(name == null) return null;
        return name.concat(" ").concat(" ").concat(price.toString()).concat(" ").concat(currency.getTitle());
    }
    @Transient
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