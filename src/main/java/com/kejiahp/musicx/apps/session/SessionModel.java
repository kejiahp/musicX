package com.kejiahp.musicx.apps.session;

import com.kejiahp.musicx.apps.user.UserModel;
import com.kejiahp.musicx.util.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "sessions")
public class SessionModel extends BaseEntity {
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserModel user;

    @Column(name = "is_valid")
    private boolean isValid = true;
}