package com.gepardec.mega.service.api;


import com.gepardec.mega.service.model.User;

public interface UserService {
    User login(String token);
}
