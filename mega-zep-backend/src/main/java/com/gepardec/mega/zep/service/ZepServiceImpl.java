package com.gepardec.mega.zep.service;

import com.gepardec.mega.aplication.utils.DateUtils;
import com.gepardec.mega.rest.translator.EmployeeTranslator;
import com.gepardec.mega.service.model.Employee;
import com.gepardec.mega.zep.exception.ZepServiceException;
import com.gepardec.mega.zep.soap.ZepSoapProvider;
import de.provantis.zep.*;
import org.slf4j.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RequestScoped
public class ZepServiceImpl implements ZepService {

    @Inject
    Logger logger;

    @Inject
    ZepSoapPortType zepSoapPortType;

    @Inject
    ZepSoapProvider zepSoapProvider;

    @Override
    public Employee getEmployee(final String userId) {
        final ReadMitarbeiterSearchCriteriaType readMitarbeiterSearchCriteriaType = new ReadMitarbeiterSearchCriteriaType();
        readMitarbeiterSearchCriteriaType.setUserId(userId);
        return getEmployeeInternal(readMitarbeiterSearchCriteriaType, employee -> true).stream().findFirst().orElse(null);
    }

    @Override
    public List<Employee> getActiveEmployees() {
        return getEmployeeInternal(null, employee -> {
            final BeschaeftigungszeitListeType beschaeftigungszeitListeType = employee.getBeschaeftigungszeitListe();
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
        });
    }

    @Override
    public void updateEmployeesReleaseDate(final String userId, final String releaseDate) {
        logger.info("start update user {} with releaseDate {}", userId, releaseDate);

        final UpdateMitarbeiterRequestType umrt = new UpdateMitarbeiterRequestType();
        umrt.setRequestHeader(zepSoapProvider.createRequestHeaderType());

        final MitarbeiterType mitarbeiter = new MitarbeiterType();
        mitarbeiter.setUserId(userId);
        mitarbeiter.setFreigabedatum(releaseDate);
        umrt.setMitarbeiter(mitarbeiter);

        final UpdateMitarbeiterResponseType updateMitarbeiterResponseType = zepSoapPortType.updateMitarbeiter(umrt);
        final ResponseHeaderType responseHeaderType = updateMitarbeiterResponseType != null ? updateMitarbeiterResponseType.getResponseHeader() : null;

        logger.info("finish update user {} with response {}", userId, responseHeaderType != null ? responseHeaderType.getReturnCode() : null);

        if (responseHeaderType != null && Integer.parseInt(responseHeaderType.getReturnCode()) != 0) {
            throw new ZepServiceException("updateEmployeeReleaseDate failed with code: " + responseHeaderType.getReturnCode());
        }
    }

    /**
     *
     * @param readMitarbeiterSearchCriteriaType search for specific criterias in zep
     * @param mitarbeiterTypePredicate filter result based on predicate
     * @return list of employees
     */
    private List<Employee> getEmployeeInternal(final ReadMitarbeiterSearchCriteriaType readMitarbeiterSearchCriteriaType, Predicate<MitarbeiterType> mitarbeiterTypePredicate) {

        final ReadMitarbeiterRequestType readMitarbeiterRequestType = new ReadMitarbeiterRequestType();
        readMitarbeiterRequestType.setRequestHeader(zepSoapProvider.createRequestHeaderType());

        if (readMitarbeiterSearchCriteriaType != null) {
            readMitarbeiterRequestType.setReadMitarbeiterSearchCriteria(readMitarbeiterSearchCriteriaType);
        }

        final ReadMitarbeiterResponseType readMitarbeiterResponseType = zepSoapPortType.readMitarbeiter(readMitarbeiterRequestType);

        final List<Employee> result = new ArrayList<>();

        Optional.ofNullable(readMitarbeiterResponseType).flatMap(readMitarbeiterResponse -> Optional.ofNullable(readMitarbeiterResponse.getMitarbeiterListe())).ifPresent(mitarbeiterListe -> {
            result.addAll(mitarbeiterListe.getMitarbeiter().stream().filter(mitarbeiterTypePredicate).map(EmployeeTranslator::toEmployee).collect(Collectors.toList()));
        });

        return result;
    }
}
