package com.gepardec.mega.service.impl.employee;

import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.domain.utils.DateUtils;
import de.provantis.zep.BeschaeftigungszeitType;
import de.provantis.zep.MitarbeiterType;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class EmployeeMapper {

    public Employee map(MitarbeiterType mitarbeiterType) {
        if (mitarbeiterType == null) {
            return null;
        }
        return Employee.builder()
                .userId(mitarbeiterType.getUserId())
                .email(mitarbeiterType.getEmail())
                .title(mitarbeiterType.getTitel())
                .firstName(mitarbeiterType.getVorname())
                .sureName(mitarbeiterType.getNachname())
                .salutation(mitarbeiterType.getAnrede())
                .releaseDate(mitarbeiterType.getFreigabedatum())
                .workDescription(mitarbeiterType.getPreisgruppe())
                .role(mitarbeiterType.getRechte())
                .active(hasEmployeeAndActiveEmployment(mitarbeiterType))
                .build();
    }

    private boolean hasEmployeeAndActiveEmployment(final MitarbeiterType employee) {
        boolean active = false;
        if (employee.getBeschaeftigungszeitListe() != null) {
            final LocalDate now = LocalDate.now();
            final List<BeschaeftigungszeitType> employments = employee.getBeschaeftigungszeitListe().getBeschaeftigungszeit();
            active = hasOpenEmployments(employments, now) || hasEmploymentEndInTheFuture(employments, now);
        }

        return active;
    }

    private boolean hasOpenEmployments(final List<BeschaeftigungszeitType> employments, final LocalDate now) {
        return employments.stream()
                .filter(employment -> isDateLessOrEqual(employment.getStartdatum(), now))
                .anyMatch(employment -> employment.getEnddatum() == null);
    }

    private boolean hasEmploymentEndInTheFuture(final List<BeschaeftigungszeitType> employments, final LocalDate now) {
        return employments.stream()
                .filter(employment -> isDateLessOrEqual(employment.getStartdatum(), now))
                .anyMatch(employment -> isDateGreaterOrEqual(employment.getEnddatum(), now));
    }

    private boolean isDateLessOrEqual(final String date, final LocalDate now) {
        return DateUtils.parseDate(date).compareTo(now) <= 0;
    }

    private boolean isDateGreaterOrEqual(final String date, final LocalDate now) {
        return DateUtils.parseDate(date).compareTo(now) >= 0;
    }
}
