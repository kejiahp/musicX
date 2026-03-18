package com.kejiahp.musicx.util;

import org.springframework.security.authentication.AnonymousAuthenticationToken;

import com.kejiahp.musicx.apps.user.UserModel;

public class VerifyPrincipalObjectType {
    public static Boolean isUserModel(Object principalObject) {
        return principalObject instanceof UserModel;
    }

    public static Boolean isAnonymousUser(Object principalObject) {
        return principalObject instanceof AnonymousAuthenticationToken;
    }
}
