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
import java.util.List;
import java.util.stream.Collectors;

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
        final com.gepardec.mega.db.entity.User user = userRepository.findActiveByEmail(email)
                .orElseThrow(() -> new ForbiddenException("User with email '" + email + "' is either unknown or inactive"));

        final Employee employee = employeeService.getEmployee(user.getZepId());

        if (employee == null) {
            throw new ForbiddenException("Could not find ZEP-User with userId '" + user.getZepId() + "'");
        }

        return User.builder()
                .dbId(user.getId())
                .userId(employee.userId())
                .email(employee.email())
                .firstname(employee.firstName())
                .lastname(employee.sureName())
                .role(Role.forId(employee.role()).orElse(null))
                .pictureUrl(pictureUrl)
                .build();
    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public List<User> getActiveUsers() {
        final List<com.gepardec.mega.db.entity.User> activeUsers = userRepository.findActive();
        return activeUsers.stream()
                .map(user -> User.builder()
                        .dbId(user.getId())
                        .userId(user.getZepId())
                        // TODO: temporary, refactor employee <-> user handling
                        .firstname("test")
                        .lastname("test")
                        .email(user.getEmail()).build())
                .collect(Collectors.toList());
    }
}
