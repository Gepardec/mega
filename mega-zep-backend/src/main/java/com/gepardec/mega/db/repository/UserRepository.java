package com.gepardec.mega.db.repository;

import com.gepardec.mega.db.entity.employee.User;
import com.gepardec.mega.domain.model.Role;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
//@Transactional seems to create an error while procuring the user data from DB TODO
public class UserRepository implements PanacheRepository<User> {

    @Inject
    EntityManager em;

    public Optional<User> findActiveByEmail(final String email) {
        return find("#User.findActiveByEmail", Parameters.with("email", email)).firstResultOptional();
    }

    public List<User> findActive() {
        return find("#User.findActive").list();
    }

    public List<User> findByRoles(final List<Role> roles) {
        return find("#User.findByRoles", Parameters.with("roles", roles)).list();
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
