package com.kejiahp.musicx.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class IsAuthUserService {
    public Boolean isUserAuthenticated() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null) {
            return false;
        }

        if (VerifyPrincipalObjectType.isAnonymousUser(auth.getPrincipal())) {
            return false;
        }

        if (VerifyPrincipalObjectType.isUserModel(auth.getPrincipal())) {
            return true;
        }

        return false;
    }
}
