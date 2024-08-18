package com.eshop.app.service;

import com.eshop.app.filter.RoleFilter;
import com.eshop.app.model.RoleModel;

public interface RoleService extends BaseService<RoleFilter,RoleModel, Long> {
    RoleModel findByRole(String role);
}
