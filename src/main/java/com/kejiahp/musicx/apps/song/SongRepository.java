package com.kejiahp.musicx.apps.song;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kejiahp.musicx.apps.album.AlbumModel;
import com.kejiahp.musicx.apps.artist.ArtistModel;

public interface SongRepository extends JpaRepository<SongModel, UUID> {
    public List<SongModel> findByArtist(ArtistModel artist);

    public List<SongModel> findByAlbum(AlbumModel album);
}