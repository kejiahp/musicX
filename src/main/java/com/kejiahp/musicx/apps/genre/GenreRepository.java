package com.kejiahp.musicx.apps.genre;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<GenreModel, UUID> {

}
