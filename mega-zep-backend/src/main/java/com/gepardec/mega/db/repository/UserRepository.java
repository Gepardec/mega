package com.gepardec.mega.db.repository;

import com.gepardec.mega.db.entity.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {

    public Optional<User> findByEmail(final String email) {
        return find("#User.findByEmail", email).firstResultOptional();
    }
}
