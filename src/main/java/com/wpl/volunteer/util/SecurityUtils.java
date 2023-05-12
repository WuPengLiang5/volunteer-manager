package com.wpl.volunteer.util;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
public class SecurityUtils {
    /**
     * 获取当前用户信息
     * @return 用户信息
     */
    public Object getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            return (UserDetails) principal;
        }

        if (principal instanceof Principal) {
            return (Principal) principal;
        }

        return null;
    }

    /**
     * 获取当前用户名
     * @return
     */
    public String getCurrentUserName() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }

        if (principal instanceof Principal) {
            return ((Principal) principal).getName();
        }

        return String.valueOf(principal);
    }
}
