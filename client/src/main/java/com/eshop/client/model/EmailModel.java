package com.eshop.client.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
public class EmailModel {

    @Email
    @NotBlank
    @NotEmpty
    private String email;

    public EmailModel setEmail(String email) {
        this.email = email;
        return this;
    }
}
