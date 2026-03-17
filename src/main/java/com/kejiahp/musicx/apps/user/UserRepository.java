package com.kejiahp.musicx.apps.user;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserModel, UUID> {
    public Optional<UserModel> findByEmail(String email);
}
