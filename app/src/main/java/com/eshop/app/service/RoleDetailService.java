package com.eshop.app.service;

import com.eshop.app.enums.CurrencyType;
import com.eshop.app.enums.NetworkType;
import com.eshop.app.filter.RoleDetailFilter;
import com.eshop.app.model.RoleDetailModel;

import javax.validation.constraints.NotNull;

public interface RoleDetailService extends BaseService<RoleDetailFilter,RoleDetailModel, Long> {
    RoleDetailModel findByRoleNameAndNetworkAndCurrency(String role, NetworkType network, @NotNull CurrencyType currency);
}
