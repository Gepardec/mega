package com.gepardec.mega.zep.service.impl;

import com.gepardec.mega.model.google.GoogleUser;
import com.gepardec.mega.security.AuthorizationInterceptor;
import com.gepardec.mega.zep.service.api.WorkerService;
import de.provantis.zep.*;
import org.apache.http.HttpStatus;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.interceptor.Interceptors;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Interceptors(AuthorizationInterceptor.class)
@ApplicationScoped
public class WorkerServiceImpl implements WorkerService {

    @Inject
    @Named("ZepAuthorizationSOAPPortType")
    ZepSoapPortType zepSoapPortType;

    @Inject
    @Named("ZepAuthorizationRequestHeaderType")
    RequestHeaderType requestHeaderType;

    private static final ReadMitarbeiterRequestType readMitarbeiterRequestType = new ReadMitarbeiterRequestType();
    private static final DateTimeFormatter DEFAULT_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault());

    @PostConstruct
    private void init() {
        readMitarbeiterRequestType.setRequestHeader(requestHeaderType);
    }

    @Override
    public MitarbeiterType getEmployee (final GoogleUser user) {
        try {
            final List<MitarbeiterType> employees = flatMap(zepSoapPortType.readMitarbeiter(readMitarbeiterRequestType));
            return employees.stream().filter(e -> e.getEmail() != null && e.getEmail().equals(user.getEmail())).findFirst().orElse(null);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<MitarbeiterType> getAllEmployees (final GoogleUser user) {
        ReadMitarbeiterResponseType rmrt = zepSoapPortType.readMitarbeiter(readMitarbeiterRequestType);
        return filterActiveEmployees(rmrt);
    }

    @Override
    public Integer updateEmployees (final List<MitarbeiterType> employees) {
        final List<Integer> statusCodeList = new LinkedList<>();

        employees.forEach(e -> statusCodeList.add(updateEmployee(e)));

        return statusCodeList.stream().filter(statuscode -> statuscode == HttpStatus.SC_INTERNAL_SERVER_ERROR).findAny().orElse(HttpStatus.SC_OK);
    }

    @Override
    public Integer updateEmployee (final MitarbeiterType employee) {
        try {
            final UpdateMitarbeiterRequestType umrt = new UpdateMitarbeiterRequestType();
            umrt.setRequestHeader(requestHeaderType);
            umrt.setMitarbeiter(employee);

            final UpdateMitarbeiterResponseType updateMitarbeiterResponseType = zepSoapPortType.updateMitarbeiter(umrt);
            final ResponseHeaderType responseHeaderType = updateMitarbeiterResponseType != null ? updateMitarbeiterResponseType.getResponseHeader() : null;

            return responseHeaderType != null ? Integer.parseInt(responseHeaderType.getReturnCode()) : HttpStatus.SC_INTERNAL_SERVER_ERROR;
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return HttpStatus.SC_INTERNAL_SERVER_ERROR;
    }

    private List<MitarbeiterType> filterActiveEmployees(ReadMitarbeiterResponseType readMitarbeiterResponseType) {
        final List<MitarbeiterType> activeEmployees = new ArrayList<>();
        final List<MitarbeiterType> allEmployees = flatMap(readMitarbeiterResponseType);

        allEmployees.forEach(employee -> {
            final BeschaeftigungszeitListeType beschaeftigungszeitListeType = employee.getBeschaeftigungszeitListe();
            final List<BeschaeftigungszeitType> beschaeftigungszeitTypeList = beschaeftigungszeitListeType.getBeschaeftigungszeit();
            final BeschaeftigungszeitType last = beschaeftigungszeitTypeList
                    .stream()
                    .sorted(Comparator.comparing(BeschaeftigungszeitType::getEnddatum))
                    .reduce((first, second) -> second)
                    .orElse(null);

            final LocalDate endDate = LocalDate.parse(Objects.requireNonNull(last).getEnddatum(), DEFAULT_DATE_FORMATTER);
            if(!endDate.isBefore(LocalDate.now())) {
                activeEmployees.add(employee);
            }
        });

        return activeEmployees;
    }

    private List<MitarbeiterType> flatMap(final ReadMitarbeiterResponseType readMitarbeiterResponseType) {
        final MitarbeiterListeType mitarbeiterListeType = readMitarbeiterResponseType.getMitarbeiterListe();
        return mitarbeiterListeType != null ? mitarbeiterListeType.getMitarbeiter() : new LinkedList<>();
    }
}
