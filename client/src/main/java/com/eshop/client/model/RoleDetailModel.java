package com.eshop.client.model;

import com.eshop.client.enums.CurrencyType;
import com.eshop.client.enums.NetworkType;
import lombok.Data;

@Data
public class RoleDetailModel extends BaseModel<Long> {
	private RoleModel role;
	private NetworkType network;
	private CurrencyType currency;
	private String address;
	private String description;
}
