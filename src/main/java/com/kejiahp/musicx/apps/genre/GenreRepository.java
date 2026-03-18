package com.kejiahp.musicx.apps.genre;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<GenreModel, UUID> {
    public Optional<GenreModel> findByName(String name);
}
