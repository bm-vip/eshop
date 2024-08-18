package com.eshop.app.repository;

import com.eshop.app.entity.SubscriptionPackageEntity;
import com.eshop.app.enums.CurrencyType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface SubscriptionPackageRepository extends BaseRepository<SubscriptionPackageEntity, Long> {
    @Query("SELECT sp FROM SubscriptionPackageEntity sp WHERE :amount BETWEEN sp.price AND sp.maxPrice AND sp.currency = :currencyType")
    Optional<SubscriptionPackageEntity> findMatchedPackageByAmountAndCurrency(BigDecimal amount, CurrencyType currencyType);
    SubscriptionPackageEntity findTopByOrderByMaxPriceDesc();
}
