package com.gepardec.mega.service.api.user;


import com.gepardec.mega.domain.model.user.User;

public interface UserService {
    User login(String token);
}
