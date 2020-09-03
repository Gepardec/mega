package com.gepardec.mega.service.impl.user;

import com.gepardec.mega.service.api.employee.EmployeeService;
import com.gepardec.mega.service.impl.user.UserServiceImpl;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private Logger logger;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private GoogleIdToken googleIdToken;

    @Mock
    private EmployeeService employeeService;

    private UserServiceImpl beanUnderTest;

    @BeforeEach
    void setUp() {
        beanUnderTest = new UserServiceImpl(logger, employeeService);
        Mockito.when(googleIdToken.getPayload()).thenReturn(new GoogleIdToken.Payload().setEmail("Thomas_0@gepardec.com").set("picture", "https://www.gepardec.com/mypicture.jpg"));
    }

}
