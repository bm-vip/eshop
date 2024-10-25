package com.eshop.client.filter;

import lombok.Setter;
import lombok.ToString;

import java.util.Optional;

@Setter
@ToString
public class NotificationFilter {
    private Long id;
    private Long senderId;
    private Long recipientId;
    private String subject;
    private String body;
    private Boolean read;


    public Optional<Long> getId() {
        return Optional.ofNullable(id);
    }

    public Optional<Long> getSenderId() {
        return Optional.ofNullable(senderId);
    }

    public Optional<Long> getRecipientId() {
        return Optional.ofNullable(recipientId);
    }

    public Optional<String> getSubject() {
        if (subject == null || subject.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(subject);
    }

    public Optional<String> getBody() {
        if (body == null || body.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(body);
    }

    public Optional<Boolean> getRead() {
        return Optional.ofNullable(read);
    }
}
