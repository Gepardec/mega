package com.gepardec.mega.service.mapper;

import com.gepardec.mega.domain.model.User;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserMapper {

    public User map(final com.gepardec.mega.db.entity.employee.User user) {
        if (user == null) {
            return null;
        }
        return User.builder()
                .dbId(user.getId())
                .userId(user.getZepId())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .releaseDate(user.getReleaseDate())
                .roles(user.getRoles())
                .build();
    }
}
