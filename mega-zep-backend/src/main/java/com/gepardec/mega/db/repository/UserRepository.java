package com.gepardec.mega.db.repository;

import com.gepardec.mega.db.entity.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {

    @Inject
    EntityManager em;

    public Optional<User> findActiveByEmail(final String email) {
        return find("#User.findActiveByEmail", Map.of("email", email)).firstResultOptional();
    }

    public List<User> findActive() {
        return find("#User.findActive").list();
    }

    public User persistOrUpdate(final User user) {
        if (user.getId() == null) {
            persist(user);
            return user;
        } else {
            return update(user);
        }
    }

    public User update(final User user) {
        return em.merge(user);
    }
}
