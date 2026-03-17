package com.kejiahp.musicx.apps.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kejiahp.musicx.apps.session.SessionModel;
import com.kejiahp.musicx.apps.session.SessionRepository;
import com.kejiahp.musicx.security.JwtAuthService;
import com.kejiahp.musicx.util.EmailValidator;
import com.kejiahp.musicx.util.exceptions.validation.EmailAlreadyExistsException;
import com.kejiahp.musicx.util.exceptions.validation.InvalidCredentials;
import com.kejiahp.musicx.util.exceptions.validation.InvalidEmailException;
import com.kejiahp.musicx.util.exceptions.validation.RecordNotFound;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtAuthService jwtAuthService;

    public UserModel createUser(String username, String email, String password) {
        if (!EmailValidator.isValid(email)) {
            throw new InvalidEmailException();
        }

        // Check if same email user exists
        if (userRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyExistsException();
        }

        UserModel user = new UserModel();
        user.setName(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    @Transactional
    public String authenticateUser(String email, String password) {
        UserModel user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RecordNotFound("User not found", "email"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentials();
        }

        // Invalidate all existing user sessions
        sessionRepository.invalidateSessions(user);

        // Create a new user session
        SessionModel newSes = new SessionModel();
        newSes.setUser(user);
        newSes.setIsValid(true);
        newSes = sessionRepository.save(newSes);

        // Generate a new authentication token
        String token = jwtAuthService.generateToken(user, newSes.getId().toString());

        return token;
    }

}