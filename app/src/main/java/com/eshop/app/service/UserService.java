package com.eshop.app.service;

import com.eshop.app.filter.UserFilter;
import com.eshop.app.model.UserModel;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public interface UserService extends BaseService<UserFilter, UserModel, UUID> {
    UserModel findByUserNameOrEmail(String login);
    UserModel findByUserName(String userName);
    UserModel register(UserModel model);
    UserModel findByEmail(String email);
    long countAllActiveChild(UUID id);
}
