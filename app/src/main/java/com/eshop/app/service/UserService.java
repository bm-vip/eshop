package com.eshop.app.service;

import com.eshop.app.filter.UserFilter;
import com.eshop.app.model.UserModel;

public interface UserService extends BaseService<UserFilter, UserModel, Long> {
    UserModel findByUserName(String userName);
    UserModel register(UserModel model);
}
