package com.eshop.app.model;

import com.eshop.app.enums.EntityStatusType;
import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expireDate;
    private Integer discountPercentage;
    private BigDecimal finalPrice;
    private String role;
    private EntityStatusType status = EntityStatusType.Pending;
    public long getRemainingWithdrawalPerDay() {
        if(createdDate == null || subscriptionPackage == null)
            return 0L;
        var endDate = createdDate.plusDays(subscriptionPackage.getWithdrawalDurationPerDay());
        long daysRemaining = ChronoUnit.DAYS.between(LocalDate.now(), endDate);
        return daysRemaining > 0L ? daysRemaining :  0L;
    }
}
