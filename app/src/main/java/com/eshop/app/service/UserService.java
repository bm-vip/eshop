package com.eshop.app.service;

import com.eshop.app.filter.UserFilter;
import com.eshop.app.model.UserModel;

import java.util.UUID;

public interface UserService extends BaseService<UserFilter, UserModel, UUID> {
    UserModel findByUserName(String userName);
    UserModel register(UserModel model);
}
