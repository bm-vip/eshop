package com.eshop.app.controller.impl;

import com.eshop.app.filter.UserFilter;
import com.eshop.app.model.UserModel;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.eshop.app.service.UserService;

import javax.validation.Valid;

@RestController
@Tag(name = "User Rest Service v1")
@RequestMapping(value = "/api/v1/user")
public class UserRestController extends BaseRestControllerImpl<UserFilter, UserModel, Long> {

    private UserService userService;

    public UserRestController(UserService service) {
        super(service, UserFilter.class);
        this.userService = service;
    }

    @PostMapping("/register")
    public ResponseEntity<UserModel> register(@Valid @RequestBody UserModel model) {
        return ResponseEntity.ok(userService.register(model));
    }
}
