package com.gepardec.mega.db.repository;

import com.gepardec.mega.db.entity.Employee;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class EmployeeRepository implements PanacheRepository<Employee> {

    public Optional<Employee> findByEmail(final String email) {
        return find("#Employee.findByEmail", email).firstResultOptional();
    }
}
