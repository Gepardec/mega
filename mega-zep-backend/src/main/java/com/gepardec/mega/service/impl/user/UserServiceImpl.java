package com.gepardec.mega.service.impl.user;

import com.gepardec.mega.application.exception.ForbiddenException;
import com.gepardec.mega.db.repository.UserRepository;
import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.domain.model.Role;
import com.gepardec.mega.domain.model.User;
import com.gepardec.mega.service.api.employee.EmployeeService;
import com.gepardec.mega.service.api.user.UserService;
import io.quarkus.cache.CacheKey;
import io.quarkus.cache.CacheResult;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@ApplicationScoped
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final EmployeeService employeeService;

    @Inject
    public UserServiceImpl(final UserRepository userRepository,
                           final EmployeeService employeeService) {
        this.userRepository = userRepository;
        this.employeeService = employeeService;
    }

    @CacheResult(cacheName = "user-email")
    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public User getUser(@CacheKey final String email, final String pictureUrl) {
        final String zepId = userRepository.findActiveByEmail(email)
                .orElseThrow(() -> new ForbiddenException("User with email '" + email + "' is either unknown or inactive"))
                .getZepId();
        final Employee employee = employeeService.getEmployee(zepId);

        if (employee == null) {
            throw new ForbiddenException("Could not find ZEP-User with userId '" + zepId + "'");
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
