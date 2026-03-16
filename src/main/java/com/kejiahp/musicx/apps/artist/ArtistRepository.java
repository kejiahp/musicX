package com.kejiahp.musicx.apps.artist;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository extends JpaRepository<ArtistModel, UUID> {
}
