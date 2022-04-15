package com.gepardec.mega.db.repository;

import com.gepardec.mega.db.entity.employee.User;
import com.gepardec.mega.domain.model.Role;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@QuarkusTest
class UserRepositoryTest {
    private static final String EMAIL = "max.muster@gepardec.com";

    @Inject
    UserRepository userRepository;

    private User user;
    private User persistedUser;

    @BeforeEach
    void init() {
        user = initializeUserObject();
        persistedUser = userRepository.persistOrUpdate(user);
    }

    @AfterEach
    void tearDown() {
        assertThat(userRepository.deleteById(user.getId())).isTrue();
    }

    @Test
    void findActiveByEmail() {
        Optional<User> userByEmail = userRepository.findActiveByEmail(persistedUser.getEmail());

        assertAll(
            () -> assertThat(userByEmail).isPresent(),
            () -> assertThat(userByEmail.get().getEmail()).isEqualTo(EMAIL)
        );
    }

    @Test
    void findActive() {
        List<User> activeUsers = userRepository.findActive();

        assertAll(
            () -> assertThat(activeUsers.get(0)).isNotNull(),
            () -> assertThat(activeUsers.get(0).getActive()).isTrue(),
            () -> assertThat(activeUsers.get(0).getEmail()).isEqualTo(EMAIL)
        );
    }

    @Test
    void findByRoles() {
        List<Role> roles = new ArrayList<>();

        roles.add(Role.EMPLOYEE);
        roles.add(Role.PROJECT_LEAD);
        roles.add(Role.OFFICE_MANAGEMENT);

        List<User> usersByRoles = userRepository.findByRoles(roles);

        assertAll(
            () -> assertThat(usersByRoles.get(0)).isNotNull(),
            () -> assertThat(usersByRoles.get(0).getEmail()).isEqualTo(EMAIL)
        );
    }

    @Test
    void persistOrUpdate() {
        assertAll(
            () -> assertThat(persistedUser).isNotNull(),
            () -> assertThat(persistedUser.getEmail()).isEqualTo(EMAIL)
        );
    }

    @Test
    void update() {
        user.setActive(false);
        userRepository.update(user);

        assertAll(
            () -> assertThat(user).isNotNull(),
            () -> assertThat(user.getActive()).isFalse(),
            () -> assertThat(user.getEmail()).isEqualTo(EMAIL)
        );
    }

    private User initializeUserObject() {
        User initUser = new User();
        initUser.setActive(true);
        initUser.setEmail(EMAIL);
        initUser.setFirstname("Max");
        initUser.setLastname("Muster");
        initUser.setLocale(Locale.GERMAN);
        initUser.setZepId("026-mmuster");
        initUser.setRoles(Set.of(Role.EMPLOYEE));
        initUser.setCreationDate(LocalDateTime.of(2021, 1, 18, 10, 10));
        initUser.setUpdatedDate(LocalDateTime.now());

        return initUser;
    }
}