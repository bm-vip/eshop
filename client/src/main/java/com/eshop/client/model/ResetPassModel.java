package com.eshop.client.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
public class ResetPassModel {
    @NotBlank
    @NotEmpty
    private String login;
    @NotBlank
    @NotEmpty
    private String otp;
    @NotBlank
    @NotEmpty
    private String newPassword;
}
