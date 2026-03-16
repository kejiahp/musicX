package com.kejiahp.musicx.apps.genre;

import com.kejiahp.musicx.util.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
}
