package com.gepardec.mega.service.impl;

import com.gepardec.mega.aplication.security.ForbiddenException;
import com.gepardec.mega.aplication.security.Role;
import com.gepardec.mega.aplication.security.SessionUser;
import com.gepardec.mega.service.api.EmployeeService;
import com.gepardec.mega.service.model.Employee;
import com.gepardec.mega.service.model.User;
import com.gepardec.mega.util.EmployeeTestUtil;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private Logger logger;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private GoogleIdToken googleIdToken;

    @Mock
    private GoogleIdTokenVerifier tokenVerifier;

    @Mock
    private EmployeeService employeeService;

    @Mock
    private SessionUser sessionUser;

    private UserServiceImpl beanUnderTest;

    @BeforeEach
    void setUp() {
        beanUnderTest = new UserServiceImpl(logger, tokenVerifier, employeeService, sessionUser);
        Mockito.when(googleIdToken.getPayload()).thenReturn(new GoogleIdToken.Payload().setEmail("Thomas_0@gepardec.com").set("picture", "https://www.gepardec.com/mypicture.jpg"));
    }

    @Test
    void testLoginTokenInvalid() throws GeneralSecurityException, IOException {
        Mockito.when(tokenVerifier.verify(Mockito.anyString())).thenReturn(null);

        final IllegalStateException illegalStateException = Assertions.assertThrows(IllegalStateException.class, () -> beanUnderTest.login("sometoken"));
        Assertions.assertEquals("Could not verify idToken", illegalStateException.getMessage());
    }

    @Test
    void testLoginEmployeesNull() throws GeneralSecurityException, IOException {
        Mockito.when(tokenVerifier.verify(Mockito.anyString())).thenReturn(googleIdToken);
        Mockito.when(employeeService.getAllActiveEmployees()).thenReturn(null);

        final ForbiddenException forbiddenException = Assertions.assertThrows(ForbiddenException.class, () -> beanUnderTest.login("sometoken"));
        Assertions.assertEquals("'Thomas_0@gepardec.com' is not an employee in ZEP", forbiddenException.getMessage());
    }

    @Test
    void testLoginEmployeesEmailNotFound() throws GeneralSecurityException, IOException {
        Mockito.when(tokenVerifier.verify(Mockito.anyString())).thenReturn(googleIdToken);
        Mockito.when(employeeService.getAllActiveEmployees()).thenReturn(Collections.singletonList(new Employee()));

        final ForbiddenException forbiddenException = Assertions.assertThrows(ForbiddenException.class, () -> beanUnderTest.login("sometoken"));
        Assertions.assertEquals("'Thomas_0@gepardec.com' is not an employee in ZEP", forbiddenException.getMessage());
    }

    @Test
    void testLogin() throws GeneralSecurityException, IOException {
        Mockito.when(tokenVerifier.verify(Mockito.anyString())).thenReturn(googleIdToken);
        Mockito.when(employeeService.getAllActiveEmployees()).thenReturn(Collections.singletonList(EmployeeTestUtil.createEmployee(0)));

        final User user = beanUnderTest.login("sometoken");

        Mockito.verify(sessionUser).init("0", "Thomas_0@gepardec.com", "sometoken", Role.USER.roleId);

        Assertions.assertEquals("Thomas_0@gepardec.com", user.getEmail());
        Assertions.assertEquals("Thomas_0", user.getFirstname());
        Assertions.assertEquals("Thomas_0_Nachname", user.getLastname());
        Assertions.assertEquals(Role.USER, user.getRole());
        Assertions.assertEquals("https://www.gepardec.com/mypicture.jpg", user.getPictureUrl());
    }
}
