package com.kejiahp.musicx.apps.user;

import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import com.kejiahp.musicx.util.exceptions.authorization.UnauthorizedException;

@Controller
public class UserController {

    @QueryMapping
    public UserModel me() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null) {
            throw new UnauthorizedException();
        }

        System.out.print("auth.isAuthenticated()");
        System.out.println(auth.isAuthenticated());

        Object principal = auth.getPrincipal();
        System.out.print("auth.getPrincipal()");
        System.out.println(principal);

        if (principal instanceof UserModel userModel) {
            System.out.println(userModel);
        }

        if (principal instanceof AnonymousAuthenticationToken) {
            System.out.println("Anonymous user");
        }

        UserModel user = (UserModel) auth.getPrincipal();

        return user;
    }
}
