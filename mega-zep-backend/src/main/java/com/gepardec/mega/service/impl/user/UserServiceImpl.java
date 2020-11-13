package com.gepardec.mega.service.impl.user;

import com.gepardec.mega.application.exception.ForbiddenException;
import com.gepardec.mega.db.repository.UserRepository;
import com.gepardec.mega.domain.model.Role;
import com.gepardec.mega.domain.model.User;
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

    @Inject
    UserMapper mapper;

    @Inject
    UserRepository userRepository;

    @CacheResult(cacheName = "user-email")
    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public User findUserForEmail(@CacheKey final String email) {
        final com.gepardec.mega.db.entity.User user = userRepository.findActiveByEmail(email)
                .orElseThrow(() -> new ForbiddenException("User with email '" + email + "' is either unknown or inactive"));

        return mapper.map(user);
    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public List<User> findActiveUsers() {
        final List<com.gepardec.mega.db.entity.User> activeUsers = userRepository.findActive();
        return activeUsers.stream()
                .map(mapper::map)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public List<User> findByRoles(Role... roles) {
        if (roles == null || roles.length == 0) {
            throw new IllegalArgumentException("Cannot load users if no 'roles' are given");
        }

        return userRepository.findByRoles(roles).stream()
                .map(mapper::map)
                .collect(Collectors.toList());
    }
}
