package com.gepardec.mega.service.impl.employee;

import com.gepardec.mega.db.repository.UserRepository;
import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.zep.ZepService;
import com.gepardec.mega.zep.ZepServiceException;
import org.eclipse.microprofile.context.ManagedExecutor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceImplTest {

    @Mock
    private Logger logger;

    @Mock
    private ZepService zepService;

    @Mock
    private ManagedExecutor managedExecutor;

    @Mock
    private UserRepository userRepository;

    private EmployeeServiceImpl beanUnderTest;

    @BeforeEach
    void setUp() {
        beanUnderTest = new EmployeeServiceImpl(logger, zepService, managedExecutor, userRepository, 10);
    }

    @Test
    void testGetEmployee() {
        Mockito.when(zepService.getEmployee(Mockito.any())).thenReturn(createEmployee(0));

        final Employee employee = beanUnderTest.getEmployee("someuserid");
        assertThat(employee).isNotNull();
        assertThat(employee.userId()).isEqualTo("0");
        assertThat(employee.firstname()).isEqualTo("Max_0");
    }

    @Test
    void testGetEmployees() {
        final Employee employee0 = createEmployee(0);
        final Employee employee1 = createEmployeeWithActive(1, false);

        Mockito.when(zepService.getEmployees()).thenReturn(List.of(employee0, employee1));

        final List<Employee> employees = beanUnderTest.getAllActiveEmployees();
        assertThat(employees).isNotNull();
        assertThat(employees).hasSize(1);
        assertThat(employees.get(0).userId()).isEqualTo("0");
        assertThat(employees.get(0).firstname()).isEqualTo("Max_0");
    }

    @Test
    void testUpdateEmployeesReleaseDate_EmployeesNull() {
        assertThatThrownBy(() -> beanUnderTest.updateEmployeesReleaseDate(null)).isInstanceOf(ZepServiceException.class);
    }

    @Test
    void testUpdateEmployeesReleaseDate_EmployeesEmpty() {
        assertThat(beanUnderTest.updateEmployeesReleaseDate(new ArrayList<>())).isEmpty();
    }

    @Test
    void testUpdateEmployeesReleaseDate_EmployeesNotEmpty_EmployeeError() {
        Mockito.doThrow(new ZepServiceException()).when(zepService).updateEmployeesReleaseDate(Mockito.any(), Mockito.any());
        Mockito.doAnswer(invocation -> {
            ((Runnable) invocation.getArgument(0)).run();
            return null;
        }).when(managedExecutor).execute(Mockito.any());

        final List<String> result = beanUnderTest.updateEmployeesReleaseDate(List.of(createEmployee(0)));
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo("0");
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

        final List<Employee> employees = IntStream.range(0, 40).mapToObj(this::createEmployee).collect(Collectors.toList());

        final List<String> result = beanUnderTest.updateEmployeesReleaseDate(employees);
        assertThat(result).isNotNull();
        assertThat(result).hasSize(10);
        assertThat(result.get(0)).isEqualTo("0");
        assertThat(result.get(9)).isEqualTo("9");
    }

    @Test
    void testUpdateEmployeesReleaseDate_EmployeesNotEmpty_EmployeOk() {
        Mockito.doAnswer(invocation -> {
            ((Runnable) invocation.getArgument(0)).run();
            return null;
        }).when(managedExecutor).execute(Mockito.any());

        final List<String> result = beanUnderTest.updateEmployeesReleaseDate(List.of(createEmployee(0)));

        assertThat(result).isNotNull();
    }

    private Employee createEmployee(final int userId) {
        return createEmployeeWithActive(userId, true);
    }

    private Employee createEmployeeWithActive(final int userId, boolean active) {
        final String name = "Max_" + userId;

        final Employee employee = Employee.builder()
                .email(name + "@gepardec.com")
                .firstname(name)
                .lastname(name + "_Nachname")
                .title("Ing.")
                .userId(String.valueOf(userId))
                .salutation("Herr")
                .workDescription("ARCHITEKT")
                .releaseDate("2020-01-01")
                .active(active)
                .build();

        return employee;
    }
}
