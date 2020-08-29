package com.gepardec.mega.service.impl.user;

import com.gepardec.mega.application.security.Role;
import com.gepardec.mega.application.exception.ForbiddenException;
import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.domain.model.User;
import com.gepardec.mega.service.api.employee.EmployeeService;
import com.gepardec.mega.service.api.user.UserService;
import io.quarkus.cache.CacheKey;
import io.quarkus.cache.CacheResult;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Optional;

@ApplicationScoped
public class UserServiceImpl implements UserService {

    private final Logger logger;
    private final EmployeeService employeeService;

    @Inject
    public UserServiceImpl(final Logger logger,
                           final EmployeeService employeeService) {
        this.logger = logger;
        this.employeeService = employeeService;
    }

    @CacheResult(cacheName = "user-email")
    @Override
    public User getUser(@CacheKey final String email, final String pictureUrl) {
        final Employee employee = Optional.ofNullable(employeeService.getAllActiveEmployees()).orElse(new ArrayList<>()).stream()
                .filter(e -> email.equals(e.email()))
                .findFirst().orElse(null);

        if (employee == null) {
            throw new ForbiddenException(String.format("'%s' is not an employee in ZEP", email));
        }

        return User.builder()
                .userId(employee.userId())
                .email(employee.email())
                .firstname(employee.firstName())
                .lastname(employee.sureName())
                .role(Role.forId(employee.role()).orElse(null))
                .pictureUrl(pictureUrl)
                .build();
    }
}
