package com.gepardec.mega.zep.service.impl;

import com.gepardec.mega.service.model.Employee;
import com.gepardec.mega.zep.exception.ZepServiceException;
import com.gepardec.mega.zep.service.api.ZepService;
import com.gepardec.mega.zep.service.soap.ZepSoapProvider;
import com.gepardec.mega.zep.service.translator.EmployeeTranslator;
import de.provantis.zep.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@RequestScoped
public class ZepServiceImpl implements ZepService {

    private Logger logger;
    private ZepSoapPortType zepSoapPortType;
    private ZepSoapProvider zepSoapProvider;

    public ZepServiceImpl() {
        // no args constructor
    }

    @Inject
    public ZepServiceImpl(final Logger logger,
                          final ZepSoapPortType zepSoapPortType,
                          final ZepSoapProvider zepSoapProvider) {
        this.logger = logger;
        this.zepSoapPortType = zepSoapPortType;
        this.zepSoapProvider = zepSoapProvider;
    }

    @Override
    public Employee getEmployee(final String userId) {
        final ReadMitarbeiterSearchCriteriaType readMitarbeiterSearchCriteriaType = new ReadMitarbeiterSearchCriteriaType();
        readMitarbeiterSearchCriteriaType.setUserId(userId);
        return getEmployeeInternal(readMitarbeiterSearchCriteriaType).stream().findFirst().orElse(null);
    }

    @Override
    public List<Employee> getEmployees() {
        return getEmployeeInternal(null);
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

        final AtomicReference<String> returnCode = new AtomicReference<>(null);
        Optional.ofNullable(updateMitarbeiterResponseType).flatMap(response -> Optional.ofNullable(response.getResponseHeader())).ifPresent((header) -> returnCode.set(header.getReturnCode()));

        logger.info("finish update user {} with response {}", userId, returnCode.get());

        if (StringUtils.isNotEmpty(returnCode.get()) && Integer.parseInt(returnCode.get()) != 0) {
            throw new ZepServiceException("updateEmployeeReleaseDate failed with code: " + returnCode.get());
        }
    }

    /**
     * @param readMitarbeiterSearchCriteriaType search for specific criterias in zep
     * @return list of employees
     */
    private List<Employee> getEmployeeInternal(final ReadMitarbeiterSearchCriteriaType readMitarbeiterSearchCriteriaType) {

        final ReadMitarbeiterRequestType readMitarbeiterRequestType = new ReadMitarbeiterRequestType();
        readMitarbeiterRequestType.setRequestHeader(zepSoapProvider.createRequestHeaderType());

        if (readMitarbeiterSearchCriteriaType != null) {
            readMitarbeiterRequestType.setReadMitarbeiterSearchCriteria(readMitarbeiterSearchCriteriaType);
        }

        final ReadMitarbeiterResponseType readMitarbeiterResponseType = zepSoapPortType.readMitarbeiter(readMitarbeiterRequestType);
        final List<Employee> result = new ArrayList<>();

        Optional.ofNullable(readMitarbeiterResponseType).flatMap(readMitarbeiterResponse -> Optional.ofNullable(readMitarbeiterResponse.getMitarbeiterListe())).ifPresent(mitarbeiterListe -> {
            result.addAll(mitarbeiterListe.getMitarbeiter().stream().map(EmployeeTranslator::toEmployee).collect(Collectors.toList()));
        });

        return result;
    }
}
