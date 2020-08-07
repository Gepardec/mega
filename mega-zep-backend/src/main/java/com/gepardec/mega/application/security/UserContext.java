package com.gepardec.mega.application.security;

import com.gepardec.mega.domain.model.User;

public interface UserContext {
    boolean isLoggedIn();

    User getUser();
}
