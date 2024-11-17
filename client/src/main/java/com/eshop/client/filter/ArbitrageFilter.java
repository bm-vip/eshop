package com.eshop.client.filter;

import com.eshop.client.enums.CurrencyType;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Setter
@ToString
@Accessors(chain = true)
public class ArbitrageFilter {
    private Long id;
    private LocalDateTime createdDate;
    private LocalDateTime createdDateFrom;
    private LocalDateTime createdDateTo;
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

    public Optional<LocalDateTime> getCreatedDateFrom() {
        return Optional.ofNullable(createdDateFrom);
    }

    public Optional<LocalDateTime> getCreatedDateTo() {
        return Optional.ofNullable(createdDateTo);
    }

    public Optional<LocalDateTime> getCreatedDate() {
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
