package com.kejiahp.musicx.apps.song;

import java.util.HashSet;
import java.util.Set;

import com.kejiahp.musicx.apps.album.AlbumModel;
import com.kejiahp.musicx.apps.artist.ArtistModel;
import com.kejiahp.musicx.apps.genre.GenreModel;
import com.kejiahp.musicx.util.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "songs")
public class SongModel extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @ManyToOne(optional = false)
    @JoinColumn(name = "artist_id")
    private ArtistModel artist;

    @Column(nullable = true, name = "url")
    private String url;

    @ManyToOne
    @JoinColumn(name = "album_id")
    private AlbumModel album;

    @Column(name = "duration_seconds")
    private Integer durationSeconds;

    @ManyToMany
    @JoinTable(name = "song_genres", joinColumns = @JoinColumn(name = "song_id"), inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private Set<GenreModel> genres = new HashSet<>();
}