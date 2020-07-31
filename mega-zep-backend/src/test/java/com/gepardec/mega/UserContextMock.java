package com.gepardec.mega;

import com.gepardec.mega.application.security.Role;
import com.gepardec.mega.application.security.UserContext;
import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.domain.model.User;
import com.gepardec.mega.util.EmployeeTestUtil;
import io.quarkus.test.Mock;

@Mock
public class UserContextMock implements UserContext {

    @Override
    public boolean isLoggedIn() {
        return true;
    }

    @Override
    public User getUser() {
        final Employee employee = EmployeeTestUtil.createEmployee(0);
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
