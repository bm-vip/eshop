package com.eshop.client.controller.impl;

import com.eshop.client.config.Limited;
import com.eshop.client.filter.UserFilter;
import com.eshop.client.model.UserModel;
import com.eshop.client.repository.CountryUsers;
import com.eshop.client.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@RestController
@Tag(name = "User Rest Service v1")
@RequestMapping(value = "/api/v1/user")
public class UserRestController extends BaseRestControllerImpl<UserFilter, UserModel, UUID> {

    private final UserService userService;
    private final SessionRegistry sessionRegistry;

    public UserRestController(UserService service, SessionRegistry sessionRegistry) {
        super(service);
        this.userService = service;
        this.sessionRegistry = sessionRegistry;
    }

    @PostMapping("/register")
    @Limited(requestsPerMinutes = 3)
    public ResponseEntity<UserModel> register(@Valid @RequestBody UserModel model) {
        return ResponseEntity.ok(userService.register(model));
    }
    @GetMapping("/verify-email/{userId}/{otp}")
    @Limited(requestsPerMinutes = 3)
    public ResponseEntity<Boolean> verifyEmail(@PathVariable UUID userId,@PathVariable String otp) {
        return ResponseEntity.ok(userService.verifyEmail(userId, otp));
    }
    @GetMapping("/total-count")
    public ResponseEntity<String> totalCount(){
        return ResponseEntity.ok("2M");
    }
    @GetMapping("/total-online")
    public ResponseEntity<Integer> totalOnline(){
//        return ResponseEntity.ok(sessionRegistry.getAllPrincipals().size());
        return ResponseEntity.ok(new Random().nextInt(700000 - 200000 + 1) + 200000);
    }
    @GetMapping("/count-by-country")
    public ResponseEntity<List<CountryUsers>> findAllUserCountByCountry(){
        return ResponseEntity.ok(userService.findAllUserCountByCountry());
    }
}
