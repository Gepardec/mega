package com.gepardec.mega.service.api.user;


import com.gepardec.mega.domain.model.User;

public interface UserService {
    User login(String token);
}
