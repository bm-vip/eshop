package com.eshop.client.model;

import com.eshop.client.enums.EntityStatusType;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
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
}
