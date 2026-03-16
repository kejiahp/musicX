package com.kejiahp.musicx.apps.album;

import com.kejiahp.musicx.apps.artist.ArtistModel;
import com.kejiahp.musicx.util.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "albums")
public class AlbumModel extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Column(nullable = true, name = "cover_image")
    private String coverImage;

    @ManyToOne(optional = false)
    @JoinColumn(name = "artist_id")
    private ArtistModel artist;
}