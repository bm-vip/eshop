package com.eshop.app.config;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HoneypotAuthenticationFilter extends OncePerRequestFilter {

    private final String honeypotFieldName;

    public HoneypotAuthenticationFilter(String honeypotFieldName) {
        this.honeypotFieldName = honeypotFieldName;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (isLoginRequest(request)) {
            String honeypotValue = request.getParameter(honeypotFieldName);
            if (honeypotValue != null && !honeypotValue.isEmpty()) {
                // Bot detected - redirect to login page with error
                response.sendRedirect("/login?errorMsg=botDetected");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private boolean isLoginRequest(HttpServletRequest request) {
        return request.getMethod().equals("POST") &&
                request.getServletPath().equals("/login");
    }
}