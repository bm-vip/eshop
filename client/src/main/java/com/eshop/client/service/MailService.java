package com.eshop.client.service;

import com.eshop.client.model.UserModel;

import javax.validation.constraints.Email;

public interface MailService {
    void send(String to, String subject, String body);
    void sendOTP(String to, String subject);
    void sendVerification(@Email String email, String emailVerificationLink);
}
