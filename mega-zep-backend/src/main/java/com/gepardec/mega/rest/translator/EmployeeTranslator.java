package com.gepardec.mega.rest.translator;

import com.gepardec.mega.rest.model.Employee;
import de.provantis.zep.MitarbeiterType;

public class EmployeeTranslator {

    public static Employee toEmployee(MitarbeiterType mitarbeiterType) {
        Employee employee = new Employee();
        employee.setUserId(mitarbeiterType.getUserId());
        employee.setEMail(mitarbeiterType.getEmail());
        employee.setTitle(mitarbeiterType.getTitel());
        employee.setFirstName(mitarbeiterType.getVorname());
        employee.setSureName(mitarbeiterType.getNachname());
        employee.setSalutation(mitarbeiterType.getAnrede());
        employee.setReleaseDate(mitarbeiterType.getFreigabedatum());
        employee.setWorkDescription(mitarbeiterType.getPreisgruppe());
        employee.setRole(mitarbeiterType.getRechte());

        return employee;
    }
}
