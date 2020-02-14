package com.gepardec.mega.service.impl;


import com.gepardec.mega.service.model.Employee;
import com.gepardec.mega.zep.exception.ZepServiceException;
import com.gepardec.mega.zep.service.ZepServiceImpl;
import org.eclipse.microprofile.context.ManagedExecutor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;


@ExtendWith(MockitoExtension.class)
public class EmployeeServiceImplTest {

    @Mock
    private Logger logger;

    @Mock
    private ZepServiceImpl zepService;

    @Mock
    private ManagedExecutor managedExecutor;

    @InjectMocks
    private EmployeeServiceImpl employeeService;


    @BeforeEach
    void setUp() {
        employeeService.setEmployeeUpdateParallelExecutions(10);
    }

    @Test
    void testUpdateEmployeesReleaseDate_EmployeesNull() {
        Assertions.assertThrows(ZepServiceException.class, () -> employeeService.updateEmployeesReleaseDate(null));
    }

    @Test
    void testUpdateEmployeesReleaseDate_EmployeesEmpty() {
        Assertions.assertTrue(employeeService.updateEmployeesReleaseDate(new ArrayList<>()).isEmpty());
    }

    @Test
    void testUpdateEmployeesReleaseDate_EmployeesNotEmpty_EmployeeError() {
        Mockito.doThrow(new ZepServiceException()).when(zepService).updateEmployeesReleaseDate(Mockito.any(), Mockito.any());
        Mockito.doAnswer(invocation -> {
            ((Runnable) invocation.getArgument(0)).run();
            return null;
        }).when(managedExecutor).execute(Mockito.any());

        final List<String> result = employeeService.updateEmployeesReleaseDate(createEmployees(1));
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("0", result.get(0));
    }

    @Test
    void testUpdateEmployeesReleaseDate_EmployeesNotEmpty_ThreadingError() {
        AtomicInteger count = new AtomicInteger();

        Mockito.doAnswer(invocation -> {
            count.getAndIncrement();
            ((Runnable) invocation.getArgument(0)).run();
            if (count.get() == 1) {
                throw new ExecutionException(new IllegalStateException());
            } else {
                return null;
            }
        }).when(managedExecutor).execute(Mockito.any());

        final List<String> result = employeeService.updateEmployeesReleaseDate(createEmployees(40));
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(10, result.size());
        Assertions.assertEquals("0", result.get(0));
        Assertions.assertEquals("9", result.get(9));
    }

    @Test
    void testUpdateEmployeesReleaseDate_EmployeesNotEmpty_EmployeOk() {
        Mockito.doAnswer(invocation -> {
            ((Runnable) invocation.getArgument(0)).run();
            return null;
        }).when(managedExecutor).execute(Mockito.any());

        final List<String> result = employeeService.updateEmployeesReleaseDate(createEmployees(1));
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
    }

    private List<Employee> createEmployees(final int count) {
        final List<Employee> result = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            final Employee employee = new Employee();
            employee.setUserId("" + i);
            employee.setReleaseDate("2020-01-31");
            result.add(employee);
        }

        return result;
    }
}
