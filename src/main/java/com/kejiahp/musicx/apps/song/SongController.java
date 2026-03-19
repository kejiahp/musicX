package com.kejiahp.musicx.apps.song;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.kejiahp.musicx.util.IsAuthUserService;
import com.kejiahp.musicx.util.exceptions.authorization.UnauthorizedException;

@Controller
public class SongController {
    @Autowired
    private SongRepository songRepository;

    @Autowired
    private IsAuthUserService isAuthUserService;

    @QueryMapping
    public List<SongModel> songs() {
        if (!isAuthUserService.isUserAuthenticated()) {
            throw new UnauthorizedException();
        }
        return songRepository.findAll();
    }

    @QueryMapping
    public SongModel song(@Argument UUID id) {
        if (!isAuthUserService.isUserAuthenticated()) {
            throw new UnauthorizedException();
        }

        return songRepository.findById(id).orElse(null);
    }
}
