package com.kejiahp.musicx.apps.user;

import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import com.kejiahp.musicx.util.ApiResponse;
import com.kejiahp.musicx.util.VerifyPrincipalObjectType;
import com.kejiahp.musicx.util.exceptions.authorization.UnauthorizedException;

@Controller
public class UserController {

    @QueryMapping
    public ApiResponse<UserModel> me() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null) {
            throw new UnauthorizedException();
        }

        Object principal = auth.getPrincipal();

        if (VerifyPrincipalObjectType.isAnonymousUser(principal)) {
            throw new UnauthorizedException();
        }

        UserModel user = (UserModel) auth.getPrincipal();

        return new ApiResponse<>(true, "Authenticated User", user);
    }
}
