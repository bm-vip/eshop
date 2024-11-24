package com.eshop.client.model;

import com.eshop.client.enums.EntityStatusType;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Data
@Accessors(chain = true)
public class SubscriptionModel extends BaseModel<Long> {
    @NotNull
    private UserModel user;
    @NotNull
    private SubscriptionPackageModel subscriptionPackage;
    private LocalDateTime expireDate;
    private Integer discountPercentage;
    private BigDecimal finalPrice;
    private EntityStatusType status = EntityStatusType.Pending;
    public long getRemainingWithdrawalPerDay() {
        if(createdDate == null || subscriptionPackage == null)
            return 0L;
        var endDate = createdDate.plusDays(subscriptionPackage.getWithdrawalDurationPerDay());
        long daysRemaining = ChronoUnit.DAYS.between(LocalDate.now(), endDate);
        return daysRemaining > 0L ? daysRemaining :  0L;
    }
}
