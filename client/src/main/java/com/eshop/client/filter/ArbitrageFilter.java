package com.eshop.client.filter;

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
    private Date createdDate;
    private Date createdDateFrom;
    private Date createdDateTo;
    private Long userId;
    private Long exchangeId;
    private Long coinId;
    private Long subscriptionId;

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

}
