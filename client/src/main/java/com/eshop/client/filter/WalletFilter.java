package com.eshop.client.filter;

import com.eshop.client.enums.CurrencyType;
import com.eshop.client.enums.TransactionType;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Optional;

@Setter
@ToString
@Accessors(chain = true)
public class WalletFilter {
    private Long id;
    private BigDecimal amount;
    private BigDecimal amountFrom;
    private BigDecimal amountTo;
    private TransactionType transactionType;
    private String transactionHash;
    private CurrencyType currency;
    private Long userId;
    private Boolean active;
    private String address;

    public Optional<Long> getId() {
        return Optional.ofNullable(id);
    }
    public Optional<BigDecimal> getAmount() {
        return Optional.ofNullable(amount);
    }
    public Optional<BigDecimal> getAmountFrom() {
        return Optional.ofNullable(amountFrom);
    }
    public Optional<BigDecimal> getAmountTo() {
        return Optional.ofNullable(amountTo);
    }
    public Optional<TransactionType> getTransactionType() {
        return Optional.ofNullable(transactionType);
    }

    public Optional<String> getTransactionHash() {
        if (transactionHash == null || transactionHash.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(transactionHash);
    }

    public Optional<CurrencyType> getCurrency() {
        return Optional.ofNullable(currency);
    }
    public Optional<Long> getUserId() {
        return Optional.ofNullable(userId);
    }

    public Optional<Boolean> getActive() {
        return Optional.ofNullable(active);
    }

    public Optional<String> getAddress() {
        if (address == null || address.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(address);
    }
}
