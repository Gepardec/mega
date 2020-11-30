package com.gepardec.mega.application.producer;

import com.gepardec.mega.application.exception.UnauthorizedException;
import com.gepardec.mega.domain.model.SecurityContext;
import com.gepardec.mega.domain.model.User;
import com.gepardec.mega.domain.model.UserContext;
import com.gepardec.mega.service.api.user.UserService;
import org.apache.commons.lang3.StringUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class UserContextProducer {

    @Inject
    SecurityContext securityContext;

    @Inject
    UserService userService;

    @Produces
    @RequestScoped
    UserContext createUserContext() {
        final User user = verifyAndLoadUser();
        return UserContext.builder()
                .user(user)
                .build();
    }

    private User verifyAndLoadUser() {
        if (securityContext.email() != null) {
            return userService.findUserForEmail(securityContext.email());
        } else {
            throw new UnauthorizedException("No security context provided");
        }
    }
}
