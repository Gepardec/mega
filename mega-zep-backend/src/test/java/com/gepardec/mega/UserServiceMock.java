package com.gepardec.mega;

import com.gepardec.mega.application.security.Role;
import com.gepardec.mega.application.security.SessionUser;
import com.gepardec.mega.domain.model.User;
import com.gepardec.mega.service.api.user.UserService;
import io.quarkus.test.Mock;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@Mock
@ApplicationScoped
public class UserServiceMock implements UserService {

    @Inject
    private SessionUser sessionUser;

    private User user;

    @Override
    public User login(String idToken) {
        sessionUser.init("1337-testvorname.testnachname", user.email(), idToken, user.role().roleId);
        return user;
    }

    public static User createUser() {
        final User user = User.builder()
                .email("test@gepardec.com")
                .firstname("Testvorname")
                .lastname("Testnachname")
                .role(Role.USER)
                .pictureUrl("https://www.gepardec.com/picture")
                .build();

        return user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
