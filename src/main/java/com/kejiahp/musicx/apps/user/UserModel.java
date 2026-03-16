package com.kejiahp.musicx.apps.user;

import com.kejiahp.musicx.util.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "users")
public class UserModel extends BaseEntity {
    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = false)
    private String password;

    @Column(nullable = true)
    private String password_reset_code;
}
