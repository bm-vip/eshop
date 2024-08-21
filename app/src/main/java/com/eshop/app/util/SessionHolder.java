package com.eshop.app.util;

import com.eshop.app.model.UserModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import javax.servlet.http.HttpServletRequest;

@Component
@SessionScope
public class SessionHolder {
    private final HttpServletRequest request;
    private ObjectMapper objectMapper;

    public SessionHolder(HttpServletRequest request) {
        this.request = request;
        this.objectMapper = new ObjectMapper();
    }

    public UserModel getCurrentUser() {
        return (UserModel) request.getSession().getAttribute("currentUser");
    }
    @SneakyThrows
    public String getCurrentUserAsJsonString() {
        return  objectMapper.writeValueAsString(request.getSession().getAttribute("currentUser"));
    }
    public SecurityContextHolderAwareRequestWrapper getRequestWrapper() {
       return new SecurityContextHolderAwareRequestWrapper(request, "");
    }
}
