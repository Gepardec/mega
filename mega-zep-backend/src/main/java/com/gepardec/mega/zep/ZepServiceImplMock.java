package com.gepardec.mega.zep;

import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.domain.model.Project;
import com.gepardec.mega.domain.model.monthlyreport.ProjectEntry;
import org.slf4j.Logger;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Alternative
@RequestScoped
public class ZepServiceImplMock implements ZepService {

    private final Map<String, Employee> employees = new HashMap<>() {{
        put("008-mgattringer", createEmployee("008-mgattringer", "marko.gattringer@gepardec.com", "Marko", "Gattringer"));
        put("030-jgartner", createEmployee("030-jgartner", "julian.gartner@gepardec.com", "Julian", "Gartner"));
        put("002-eerofeev", createEmployee("002-eerofeev", "egor.erofeev@gepardec.com", "Egor", "Erofeev"));
        put("005-wbruckmueller", createEmployee("005-wbruckmueller", "werner.bruckmueller@gepardec.com", "Werner", "Bruckmüller"));
        put("e09-lhulin", createEmployee("e09-lhulin", "lukasz.hulin@gepardec.com", "Lukasz", "Hulin"));
    }};

    private final Logger logger;

    @Inject
    public ZepServiceImplMock(Logger logger) {
        this.logger = logger;
    }


    @Override
    public Employee getEmployee(String userId) {
        if(employees.containsKey(userId)) {
            logger.warn(String.format("No user with userId=%s found", userId));
            return this.employees.get(userId);
        } else {
            return null;
        }
    }

    @Override
    public List<Employee> getEmployees() {
        return null;
    }

    @Override
    public void updateEmployeesReleaseDate(String userId, String releaseDate) {

    }

    @Override
    public List<ProjectEntry> getProjectTimes(Employee employee) {
        return null;
    }

    @Override
    public List<Project> getProjectsForMonthYear(LocalDate monthYear) {
        return null;
    }

    private Employee createEmployee(String userId, String email, String firstName, String sureName) {
        return Employee.builder()
                .userId(userId)
                .firstName(firstName)
                .sureName(sureName)
                .email(email)
                //.releaseDate()
                .build();
    }
}
