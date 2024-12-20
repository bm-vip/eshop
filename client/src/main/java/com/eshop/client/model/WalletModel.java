package com.eshop.client.model;

import com.eshop.client.enums.CurrencyType;
import com.eshop.client.enums.TransactionType;
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
    private TransactionType transactionType;
    @NotNull
    private String transactionHash;
    @NotNull
    private CurrencyType currency = CurrencyType.USDT;
    private UserModel user;
    private String address;
    private boolean active = false;
    private String role;

}
