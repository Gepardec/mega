package com.gepardec.mega.service.employee;

import com.gepardec.mega.application.utils.DateUtils;
import com.gepardec.mega.domain.Employee;
import de.provantis.zep.BeschaeftigungszeitListeType;
import de.provantis.zep.BeschaeftigungszeitType;
import de.provantis.zep.MitarbeiterType;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class EmployeeTranslator {

    private EmployeeTranslator() {
        throw new IllegalStateException("Utility class");
    }

    public static Employee toEmployee(MitarbeiterType mitarbeiterType) {
        Employee employee = new Employee();
        employee.setUserId(mitarbeiterType.getUserId());
        employee.setEmail(mitarbeiterType.getEmail());
        employee.setTitle(mitarbeiterType.getTitel());
        employee.setFirstName(mitarbeiterType.getVorname());
        employee.setSureName(mitarbeiterType.getNachname());
        employee.setSalutation(mitarbeiterType.getAnrede());
        employee.setReleaseDate(mitarbeiterType.getFreigabedatum());
        employee.setWorkDescription(mitarbeiterType.getPreisgruppe());
        employee.setRole(mitarbeiterType.getRechte());
        employee.setActive(isActive(mitarbeiterType));

        return employee;
    }

    private static boolean isActive(final MitarbeiterType mitarbeiterType) {
        final BeschaeftigungszeitListeType beschaeftigungszeitListeType = Optional.ofNullable(mitarbeiterType.getBeschaeftigungszeitListe()).orElse(new BeschaeftigungszeitListeType());
        final List<BeschaeftigungszeitType> beschaeftigungszeitTypeList = beschaeftigungszeitListeType.getBeschaeftigungszeit();
        final BeschaeftigungszeitType last = beschaeftigungszeitTypeList
                .stream()
                .sorted(Comparator.comparing(BeschaeftigungszeitType::getStartdatum))
                .reduce((first, second) -> second)
                .orElse(null);

        if (last != null) {
            // if enddatum (sic!) is null => employee is active
            if (last.getEnddatum() == null) {
                return true;
            } else {
                final LocalDate endDate = DateUtils.parseDate(Objects.requireNonNull(last).getEnddatum());
                return !endDate.isBefore(LocalDate.now());
            }
        }
        return false;
    }
}
