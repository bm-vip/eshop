package com.eshop.client.model;

import com.eshop.client.enums.EntityStatusType;
import com.eshop.client.util.DateUtil;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Data
@Accessors(chain = true)
public class SubscriptionModel extends BaseModel<Long> {
    @NotNull
    private UserModel user;
    @NotNull
    private SubscriptionPackageModel subscriptionPackage;
    private Date expireDate;
    private Integer discountPercentage;
    private BigDecimal finalPrice;
    private EntityStatusType status = EntityStatusType.Pending;
    public long getRemainingWithdrawalPerDay() {
        if(createdDate == null || subscriptionPackage == null)
            return 0L;
        var endDate = DateUtil.toLocalDate(createdDate).plusDays(subscriptionPackage.getWithdrawalDurationPerDay());
        long daysRemaining = ChronoUnit.DAYS.between(LocalDate.now(), endDate);
        return daysRemaining > 0L ? daysRemaining :  0L;
    }
}
