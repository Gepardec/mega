package com.gepardec.mega.db.repository;

import com.gepardec.mega.db.entity.employee.User;
import com.gepardec.mega.domain.model.Role;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import javax.inject.Inject;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class UserRepositoryTest {

    private static final String EMAIL = "max.muster@gepardec.com";

    @Inject
    UserRepository userRepository;

    private User user;
    private User persistedUser;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setActive(true);
        user.setEmail(EMAIL);
        user.setFirstname("Max");
        user.setLastname("Muster");
        user.setLocale(Locale.GERMAN);
        user.setZepId("026-mmuster");
        user.setRoles(Set.of(Role.EMPLOYEE));
        user.setCreationDate(LocalDateTime.of(2021, 1,18, 10, 10));
        user.setUpdatedDate(LocalDateTime.now());

        persistedUser = userRepository.persistOrUpdate(user);
    }

    @AfterEach
    void tearDown() {
        boolean deleted = userRepository.deleteById(user.getId());
        assertThat(deleted).isTrue();
    }

    @Test
    void findActiveByEmail() {
        Optional<User> userByEmail = userRepository.findActiveByEmail(persistedUser.getEmail());

        assertAll(
                () -> assertThat(userByEmail).isPresent(),
                () -> assertThat(userByEmail).isPresent().isEqualTo(EMAIL)
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
}