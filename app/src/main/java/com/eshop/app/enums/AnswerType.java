package com.eshop.app.enums;

import com.eshop.app.config.MessageConfig;
import com.eshop.app.model.Select2Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

public enum AnswerType {
    STRING("String"),
    INTEGER("Integer"),
    BOOLEAN("Boolean"),
    MULTIPLE("Multiple");

    AnswerType(String title) {
        this.title = title;
    }

    private MessageConfig messages;
    private String title;
    public MessageConfig getMessages() {
        return messages;
    }

    public void setMessages(MessageConfig messages) {
        this.messages = messages;
    }

    public String getTitle() {
        return messages.getMessage(title);
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public static List<Select2Model> getAll() {
        return Arrays.stream(values()).map(m -> new Select2Model(m.name(), m.getTitle())).collect(Collectors.toList());
    }

    @Component
    public static class EnumValuesInjectionService {

        @Autowired
        private MessageConfig messages;

        //Inject into bean through static inner class and assign value to enum.
        @PostConstruct
        public void postConstruct() {

            for (AnswerType type : EnumSet.allOf(AnswerType.class)) {
                type.setMessages(messages);
            }
        }
    }
}
