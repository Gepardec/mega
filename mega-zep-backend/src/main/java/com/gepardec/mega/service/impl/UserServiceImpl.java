package com.gepardec.mega.service.impl;

import com.gepardec.mega.application.security.Role;
import com.gepardec.mega.application.security.SessionUser;
import com.gepardec.mega.application.security.exception.ForbiddenException;
import com.gepardec.mega.application.security.exception.UnauthorizedException;
import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.domain.model.User;
import com.gepardec.mega.service.api.EmployeeService;
import com.gepardec.mega.service.api.UserService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Optional;

@ApplicationScoped
public class UserServiceImpl implements UserService {

    private final Logger logger;
    private final GoogleIdTokenVerifier tokenVerifier;
    private final EmployeeService employeeService;
    private final SessionUser sessionUser;

    @Inject
    public UserServiceImpl(final Logger logger,
                           final GoogleIdTokenVerifier tokenVerifier,
                           final EmployeeService employeeService,
                           final SessionUser sessionUser) {
        this.logger = logger;
        this.tokenVerifier = tokenVerifier;
        this.employeeService = employeeService;
        this.sessionUser = sessionUser;
    }

    @Override
    public User login(final String idToken) {
        final String email;
        final String pictureUrl;

        logger.debug("login with {}", idToken);

        try {
            final GoogleIdToken.Payload payload = Optional.ofNullable(tokenVerifier.verify(idToken))
                    .orElseThrow(() -> new UnauthorizedException("IdToken was invalid"))
                    .getPayload();
            email = payload.getEmail();
            pictureUrl = payload.get("picture").toString();
        } catch (Exception e) {
            throw new IllegalStateException("Could not verify idToken", e);
        }

        final Employee employee = Optional.ofNullable(employeeService.getAllActiveEmployees()).orElse(new ArrayList<>()).stream()
                .filter(e -> email.equals(e.email()))
                .findFirst().orElse(null);

        if (employee == null) {
            throw new ForbiddenException(String.format("'%s' is not an employee in ZEP", email));
        }

        // Could be re-logged in with different user within the same session
        sessionUser.init(employee.userId(), email, idToken, employee.role());


        final User user = User.builder()
                .email(employee.email())
                .firstname(employee.firstName())
                .lastname(employee.sureName())
                .role(Role.forId(employee.role()).orElse(null))
                .pictureUrl(pictureUrl)
                .build();

        return user;
    }
}
