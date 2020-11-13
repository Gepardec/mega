package com.gepardec.mega.service.impl.user;

import com.gepardec.mega.domain.model.User;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserMapper {

    public User map(final com.gepardec.mega.db.entity.User user) {
        if (user == null) {
            return null;
        }
        return User.builder()
                .dbId(user.getId())
                .userId(user.getZepId())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .roles(user.getRoles())
                .build();
    }
}
