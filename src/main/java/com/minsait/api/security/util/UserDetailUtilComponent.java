package com.minsait.api.security.util;

import com.minsait.api.security.details.CustomUserDetails;
import org.springframework.stereotype.Component;

@Component
public class UserDetailUtilComponent {
    /**
     * Classe cirada apenas para facilitar o mock de um método estático no teste unitário
     * */
    public CustomUserDetails getAuthenticatedUser(){
        return UserDetailUtil.getAuthenticatedUser();
    }
}
