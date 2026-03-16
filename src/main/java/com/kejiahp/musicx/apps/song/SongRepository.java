package com.kejiahp.musicx.apps.song;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SongRepository extends JpaRepository<SongModel, UUID> {
}