package com.eshop.app.model;

import com.eshop.app.enums.CurrencyType;
import com.eshop.app.enums.TransactionType;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Set;

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
    private CurrencyType currency;
    private UserModel user;
    private String address;
    private boolean active;

}
