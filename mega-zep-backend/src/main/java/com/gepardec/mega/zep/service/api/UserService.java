package com.gepardec.mega.zep.service.api;


import com.gepardec.mega.rest.model.User;

public interface UserService {
    User login(String token);
}
