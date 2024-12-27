package com.eshop.app.model;

import com.eshop.app.enums.CurrencyType;
import com.eshop.app.enums.NetworkType;
import lombok.Data;

@Data
public class RoleDetailModel extends BaseModel<Long> {
	private RoleModel role;
	private NetworkType network;
	private CurrencyType currency;
	private String address;
	private String description;
}
