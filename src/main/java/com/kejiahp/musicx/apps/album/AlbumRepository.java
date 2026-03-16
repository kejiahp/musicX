package com.kejiahp.musicx.apps.album;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumRepository extends JpaRepository<AlbumModel, UUID> {
}
