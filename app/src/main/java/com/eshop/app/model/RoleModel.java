package com.eshop.app.model;

import com.eshop.app.mapping.RoleMapper;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class RoleModel extends BaseModel<Long> {
	@NotNull
	@NotBlank
	private String role;
	private String title;
}
