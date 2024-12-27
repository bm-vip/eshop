package com.eshop.app.repository;

import com.eshop.app.entity.RoleDetailEntity;
import com.eshop.app.enums.CurrencyType;
import com.eshop.app.enums.NetworkType;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleDetailRepository extends BaseRepository<RoleDetailEntity, Long>{
    Optional<RoleDetailEntity> findByRoleRoleAndNetworkAndCurrency(String role, NetworkType networkType, CurrencyType currencyType);
}
