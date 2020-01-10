package com.gepardec.mega.rest;

import de.provantis.zep.MitarbeiterType;

import java.util.Optional;

public class EmployeeAdapter {

    public static Optional<Employee> toEmployee(MitarbeiterType mitarbeiterType) {
        Employee employee = null;
        if (mitarbeiterType != null) {
            employee = new Employee();
            employee.setUserId(mitarbeiterType.getUserId());
            employee.setEMail(mitarbeiterType.getEmail());
            employee.setTitle(mitarbeiterType.getTitel());
            employee.setFirstName(mitarbeiterType.getVorname());
            employee.setSureName(mitarbeiterType.getNachname());
            employee.setSalutation(mitarbeiterType.getAnrede());
            employee.setReleaseDate(mitarbeiterType.getFreigabedatum());
            employee.setWorkDescription(mitarbeiterType.getPreisgruppe());
            employee.setRole(mitarbeiterType.getRechte());
        }
        return Optional.ofNullable(employee);
    }
}
