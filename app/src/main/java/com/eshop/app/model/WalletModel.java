package com.eshop.app.model;

import com.eshop.app.enums.CurrencyType;
import com.eshop.app.enums.EntityStatusType;
import com.eshop.app.enums.NetworkType;
import com.eshop.app.enums.TransactionType;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class WalletModel extends BaseModel<Long> {
    @NotNull
    private BigDecimal amount;
    @NotNull
    private BigDecimal actualAmount;
    @NotNull
    private TransactionType transactionType;
    @NotNull
    private String transactionHash;
    @NotNull
    private CurrencyType currency = CurrencyType.USDT;
    private NetworkType network = NetworkType.TRC20;
    private UserModel user;
    private String address;
    private EntityStatusType status = EntityStatusType.Pending;
    private String role;
    private Long roleId;

}
