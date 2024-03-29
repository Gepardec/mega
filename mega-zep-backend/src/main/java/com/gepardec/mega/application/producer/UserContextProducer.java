package com.gepardec.mega.application.producer;

import com.gepardec.mega.application.exception.UnauthorizedException;
import com.gepardec.mega.domain.model.SecurityContext;
import com.gepardec.mega.domain.model.User;
import com.gepardec.mega.domain.model.UserContext;
import com.gepardec.mega.service.api.UserService;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

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
        if (securityContext.getEmail() != null) {
            return userService.findUserForEmail(securityContext.getEmail());
        } else {
            throw new UnauthorizedException("No security context provided");
        }
    }
}
