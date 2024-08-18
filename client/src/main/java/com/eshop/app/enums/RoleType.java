package com.eshop.app.enums;

import org.springframework.security.core.context.SecurityContextHolder;

public class RoleType {
    public static final String ADMIN = "ROLE_ADMIN";
    public static final String SUPER_WISER = "ROLE_SUPER_WISER";
    public static final String GUEST = "ROLE_GUEST";
    public static final String USER = "ROLE_USER";

    public static String name(String role){
        return role.replace("ROLE_","");
    }
    public static boolean hasRole(String roleName) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            return auth.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(roleName));
        }
        return false;
    }
}
