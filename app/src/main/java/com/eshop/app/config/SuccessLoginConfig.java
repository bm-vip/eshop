package com.eshop.app.config;

import com.eshop.app.enums.RoleType;
import com.eshop.app.service.UserService;
import com.eshop.app.util.SessionHolder;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class SuccessLoginConfig implements AuthenticationSuccessHandler {
    private final UserService userService;
    private RedirectStrategy redirectStrategy;
    private SessionHolder sessionHolder;

    public SuccessLoginConfig(UserService userService, SessionHolder sessionHolder) {
        this.userService = userService;
        this.sessionHolder = sessionHolder;
        this.redirectStrategy = new DefaultRedirectStrategy();
    }
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        var userModel = userService.findByUserName(authentication.getName());
        sessionHolder.setCurrentUser(userModel);
        MDC.put("userId",userModel.getId().toString());

        SecurityContextHolderAwareRequestWrapper requestWrapper = new SecurityContextHolderAwareRequestWrapper(request, "");
        String targetUrl = "/dashboard";
        if (requestWrapper.isUserInRole(RoleType.USER))
            targetUrl = "/access-denied";
        redirectStrategy.sendRedirect(request, response, targetUrl);
    }
}
