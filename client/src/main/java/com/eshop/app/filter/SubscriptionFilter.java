package com.eshop.app.filter;

import com.eshop.app.enums.EntityStatusType;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Optional;

@Setter
@ToString
public class SubscriptionFilter {
    private Long id;
    private Long userId;
    private Long subscriptionPackageId;
    private Long expireDateFrom;
    private Long expireDateTo;
    private Integer discountPercentage;
    private BigDecimal finalPriceFrom;
    private BigDecimal finalPriceTo;
    private EntityStatusType status;

    public Optional<Long> getId() {
        return Optional.ofNullable(id);
    }

    public Optional<Long> getUserId() {
        return Optional.ofNullable(userId);
    }

    public Optional<Long> getSubscriptionPackageId() {
        return Optional.ofNullable(subscriptionPackageId);
    }

    public Optional<Long> getExpireDateFrom() {
        return Optional.ofNullable(expireDateFrom);
    }

    public Optional<Long> getExpireDateTo() {
        return Optional.ofNullable(expireDateTo);
    }

    public Optional<Integer> getDiscountPercentage() {
        return Optional.ofNullable(discountPercentage);
    }

    public Optional<BigDecimal> getFinalPriceFrom() {
        return Optional.ofNullable(finalPriceFrom);
    }

    public Optional<BigDecimal> getFinalPriceTo() {
        return Optional.ofNullable(finalPriceTo);
    }

    public Optional<EntityStatusType> getStatus() {
        return Optional.ofNullable(status);
    }
}
