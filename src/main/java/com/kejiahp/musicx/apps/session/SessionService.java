package com.kejiahp.musicx.apps.session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kejiahp.musicx.apps.user.UserModel;

@Service
public class SessionService {
    @Autowired
    private SessionRepository sessionRepository;

    public boolean isSessionValid(UserModel user, String sid) {
        boolean sessionExist = sessionRepository.findFirstByUserAndIsValidTrue(user).isPresent();
        return sessionExist;
    }
}
