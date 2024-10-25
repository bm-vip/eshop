package com.eshop.client.service;

public interface MailService {
    void send(String to, String subject, String body);
    void sendOTP(String to);
}
