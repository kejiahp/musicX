package com.kejiahp.musicx.apps.user;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kejiahp.musicx.util.exceptions.authorization.UnauthorizedException;

/**
 * Created this to break the cyclic dependency between SecurityConfig ->
 * JwtAuthFilter -> UserService
 */
@Service
public class CustomUserService {
    @Autowired
    private UserRepository userRepository;

    public Optional<UserModel> getUserById(String uuid) throws UnauthorizedException {
        return userRepository.findById(UUID.fromString(uuid));
    }
}
