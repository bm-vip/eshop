package com.eshop.client.controller.impl;

import com.eshop.client.filter.UserFilter;
import com.eshop.client.model.UserModel;
import com.eshop.client.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Tag(name = "User Rest Service v1")
@RequestMapping(value = "/api/v1/user")
public class UserRestController extends BaseRestControllerImpl<UserFilter, UserModel, Long> {

    private final UserService userService;
    private final SessionRegistry sessionRegistry;

    public UserRestController(UserService service, SessionRegistry sessionRegistry) {
        super(service, UserFilter.class);
        this.userService = service;
        this.sessionRegistry = sessionRegistry;
    }

    @PostMapping("/register")
    public ResponseEntity<UserModel> register(@Valid @RequestBody UserModel model) {
        return ResponseEntity.ok(userService.register(model));
    }
    @GetMapping("/total-count")
    public ResponseEntity<String> totalCount(){
        return ResponseEntity.ok("2M");
    }
    @GetMapping("/total-online")
    public ResponseEntity<Integer> totalOnline(){
        return ResponseEntity.ok(sessionRegistry.getAllPrincipals().size() + 1000);
    }
}
