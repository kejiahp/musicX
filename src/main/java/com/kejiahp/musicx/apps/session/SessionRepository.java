package com.kejiahp.musicx.apps.session;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.kejiahp.musicx.apps.user.UserModel;

public interface SessionRepository extends JpaRepository<SessionModel, UUID> {
    Optional<SessionModel> findFirstByUserAndIsValidTrue(UserModel user);

    List<SessionModel> findAllByUserAndIsValidTrue(UserModel user);

    @Modifying
    @Transactional
    @Query("UPDATE SessionModel s SET s.isValid = false WHERE s.user = :user AND s.isValid = true")
    void invalidateSessions(UserModel user);
}
