package com.eshop.app.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class NotificationModel extends BaseModel<Long> {
    private UserModel sender;
    private UserModel recipient;
    private List<Long> recipients;
    private boolean allRecipients;
    private String subject;
    private String body;
    private boolean read = false;
}
