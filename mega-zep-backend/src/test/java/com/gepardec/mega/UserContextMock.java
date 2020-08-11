package com.gepardec.mega;

import com.gepardec.mega.application.security.Role;
import com.gepardec.mega.application.security.UserContext;
import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.domain.model.User;
import io.quarkus.test.Mock;

@Mock
public class UserContextMock implements UserContext {

    @Override
    public boolean isLoggedIn() {
        return true;
    }

    @Override
    public User getUser() {
        final String name = "Thomas_0";
        final Employee employee = Employee.builder()
                .email(name + "@gepardec.com")
                .firstName(name)
                .sureName(name + "_Nachname")
                .title("Ing.")
                .userId(String.valueOf(0))
                .salutation("Herr")
                .workDescription("ARCHITEKT")
                .releaseDate("2020-01-01")
                .role(Role.USER.roleId)
                .active(true)
                .build();
        return User.builder()
                .userId(employee.userId())
                .email(employee.email())
                .firstname(employee.firstName())
                .lastname(employee.sureName())
                .role(Role.forId(employee.role()).orElse(null))
                .pictureUrl("https://www.gepardec.com/test.jpg")
                .build();
    }
}
