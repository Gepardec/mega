package com.gepardec.mega.service.impl.employee;

import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.domain.utils.DateUtils;
import de.provantis.zep.BeschaeftigungszeitListeType;
import de.provantis.zep.BeschaeftigungszeitType;
import de.provantis.zep.MitarbeiterType;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class EmployeeMapper {

    private EmployeeMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static Employee toEmployee(MitarbeiterType mitarbeiterType) {
        Employee employee = Employee.builder()
                .userId(mitarbeiterType.getUserId())
                .email(mitarbeiterType.getEmail())
                .title(mitarbeiterType.getTitel())
                .firstName(mitarbeiterType.getVorname())
                .sureName(mitarbeiterType.getNachname())
                .salutation(mitarbeiterType.getAnrede())
                .releaseDate(mitarbeiterType.getFreigabedatum())
                .workDescription(mitarbeiterType.getPreisgruppe())
                .role(mitarbeiterType.getRechte())
                .active(isActive(mitarbeiterType))
                .build();

        return employee;
    }

    private static boolean isActive(final MitarbeiterType mitarbeiterType) {
        final BeschaeftigungszeitListeType beschaeftigungszeitListeType = mitarbeiterType.getBeschaeftigungszeitListe();

        if(beschaeftigungszeitListeType != null && !beschaeftigungszeitListeType.getBeschaeftigungszeit().isEmpty()) {

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
        }

        return false;
    }
}
