package com.gepardec.mega.service.api;

import com.gepardec.mega.domain.model.Role;
import com.gepardec.mega.domain.model.User;

import java.util.List;

public interface UserService {

    User findUserForEmail(String email);

    List<User> findActiveUsers();

    List<User> findByRoles(List<Role> roles);
}
