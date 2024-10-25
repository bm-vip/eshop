package com.eshop.client.filter;

import com.eshop.client.enums.CurrencyType;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

@Setter
@ToString
@Accessors(chain = true)
public class ArbitrageFilter {
    private Long id;
    private Date createdDate;
    private Date createdDateFrom;
    private Date createdDateTo;
    private Long userId;
    private Long exchangeId;
    private Long coinId;
    private Long subscriptionId;
    private BigDecimal rewardFrom;
    private BigDecimal rewardTo;
    private CurrencyType currency;

    public Optional<Long> getId() {
        return Optional.ofNullable(id);
    }

    public Optional<Date> getCreatedDateFrom() {
        return Optional.ofNullable(createdDateFrom);
    }

    public Optional<Date> getCreatedDateTo() {
        return Optional.ofNullable(createdDateTo);
    }

    public Optional<Date> getCreatedDate() {
        return Optional.ofNullable(createdDate);
    }

    public Optional<Long> getUserId() {
        return Optional.ofNullable(userId);
    }

    public Optional<Long> getExchangeId() {
        return Optional.ofNullable(exchangeId);
    }

    public Optional<Long> getCoinId() {
        return Optional.ofNullable(coinId);
    }

    public Optional<Long> getSubscriptionId() {
        return Optional.ofNullable(subscriptionId);
    }

    public Optional<BigDecimal> getRewardFrom() {
        return Optional.ofNullable(rewardFrom);
    }

    public Optional<BigDecimal> getRewardTo() {
        return Optional.ofNullable(rewardTo);
    }

    public Optional<CurrencyType> getCurrency() {
        return Optional.ofNullable(currency);
    }
}
