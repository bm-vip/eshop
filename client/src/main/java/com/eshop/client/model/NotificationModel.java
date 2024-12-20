package com.eshop.client.model;

import com.eshop.client.util.DateUtil;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class NotificationModel extends BaseModel<Long> {
    private UserModel sender;
    private UserModel recipient;
    private String subject;
    private String body;
    private boolean read = false;
    private String role;
    public String getCreatedDateAgo(){
        return DateUtil.timeAgo(this.createdDate);
    }
}
