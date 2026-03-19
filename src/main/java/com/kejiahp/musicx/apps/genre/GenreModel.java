package com.kejiahp.musicx.apps.genre;

import java.util.HashSet;
import java.util.Set;

import com.kejiahp.musicx.apps.song.SongModel;
import com.kejiahp.musicx.util.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "genres")
public class GenreModel extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "genres")
    private Set<SongModel> songs = new HashSet<>();
}
