package com.gepardec.mega;

import com.gepardec.mega.application.security.Role;
import com.gepardec.mega.application.security.SessionUser;
import com.gepardec.mega.service.api.UserService;
import com.gepardec.mega.domain.model.User;
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
        sessionUser.init("1337-testvorname.testnachname", user.getEmail(), idToken, user.getRole().roleId);
        return user;
    }

    public static User createUser() {
        final User user = new User();
        //user.setUserId("1337-testvorname.testnachname");
        user.setEmail("test@gepardec.com");
        user.setFirstname("Testvorname");
        user.setLastname("Testnachname");
        user.setRole(Role.USER);
        user.setPictureUrl("https://www.gepardec.com/picture");
        return user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
