package com.eshop.app.filter;

import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.Optional;

@Setter
@ToString
@Accessors(chain = true)
public class ArbitrageFilter {
    private Long id;
    private Date createdDateFrom;
    private Date createdDateTo;
    private Long userId;
    private Long exchangeId;
    private Long coinId;
    private Long subscriptionId;

    public Optional<Long> getId() {
        return Optional.ofNullable(id);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Optional<Date> getCreatedDateFrom() {
        return Optional.ofNullable(createdDateFrom);
    }

    public void setCreatedDateFrom(Date createdDateFrom) {
        this.createdDateFrom = createdDateFrom;
    }

    public Optional<Date> getCreatedDateTo() {
        return Optional.ofNullable(createdDateTo);
    }

    public void setCreatedDateTo(Date createdDateTo) {
        this.createdDateTo = createdDateTo;
    }

    public Optional<Long> getUserId() {
        return Optional.ofNullable(userId);
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Optional<Long> getExchangeId() {
        return Optional.ofNullable(exchangeId);
    }

    public void setExchangeId(Long exchangeId) {
        this.exchangeId = exchangeId;
    }

    public Optional<Long> getCoinId() {
        return Optional.ofNullable(coinId);
    }

    public void setCoinId(Long coinId) {
        this.coinId = coinId;
    }

    public Optional<Long> getSubscriptionId() {
        return Optional.ofNullable(subscriptionId);
    }

    public void setSubscriptionId(Long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }
}
