package com.gepardec.mega.service.api.user;


import com.gepardec.mega.domain.model.User;

import java.util.List;

public interface UserService {
    User getUser(String email, String pictureUrl);

    List<User> getActiveUsers();
}
