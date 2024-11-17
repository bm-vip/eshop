package com.eshop.app.model;

import com.eshop.app.enums.EntityStatusType;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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
}
