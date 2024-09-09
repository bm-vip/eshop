package com.eshop.client.service;

import com.eshop.client.filter.UserFilter;
import com.eshop.client.model.UserModel;

public interface UserService extends BaseService<UserFilter, UserModel, Long> {
    UserModel findByUserName(String userName);
    UserModel findByEmail(String email);
    UserModel register(UserModel model);
}
