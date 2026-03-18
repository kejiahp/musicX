package com.kejiahp.musicx.apps.artist;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository extends JpaRepository<ArtistModel, UUID> {
    public Optional<ArtistModel> findByName(String name);
}
