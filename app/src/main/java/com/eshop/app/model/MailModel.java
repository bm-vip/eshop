package com.eshop.app.model;

import lombok.Data;

@Data
public class MailModel {
    private String to;
    private String subject;
    private String body;
}
