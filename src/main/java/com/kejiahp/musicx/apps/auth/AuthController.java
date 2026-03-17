package com.kejiahp.musicx.apps.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import com.kejiahp.musicx.apps.auth.dto.TokenResponse;
import com.kejiahp.musicx.apps.user.UserModel;
import com.kejiahp.musicx.apps.user.UserService;
import com.kejiahp.musicx.util.ApiResponse;

@Controller
public class AuthController {
    @Autowired
    private UserService userService;

    @MutationMapping
    public ApiResponse<UserModel> signup(@Argument String name, @Argument String email, @Argument String password) {
        UserModel user = userService.createUser(name, email, password);
        return new ApiResponse<>(true, "User successfully created", user);
    }

    @MutationMapping
    public ApiResponse<TokenResponse> login(@Argument String email, @Argument String password) {
        String token = userService.authenticateUser(email, password);
        return new ApiResponse<>(true, "Login successful", new TokenResponse(token));
    }
}
