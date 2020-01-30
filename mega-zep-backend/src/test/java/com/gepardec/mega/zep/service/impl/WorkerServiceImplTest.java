package com.gepardec.mega.zep.service.impl;


import com.gepardec.mega.aplication.security.Role;
import com.gepardec.mega.monthlyreport.MonthlyReport;
import com.gepardec.mega.monthlyreport.WorkingLocation;
import com.gepardec.mega.monthlyreport.warning.WarningConfig;
import com.gepardec.mega.zep.soap.ZepSoapProvider;
import de.provantis.zep.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import java.time.LocalDate;
import java.util.Arrays;


@ExtendWith(MockitoExtension.class)
public class WorkerServiceImplTest {

    @Mock
    private Logger logger;

    @Mock
    private ZepSoapProvider zepSoapProvider;

    @Mock
    private ZepSoapPortType zepSoapPortType;

    @Mock
    private WarningConfig warningConfig;

    @InjectMocks
    private WorkerServiceImpl workerService;

    @BeforeEach
    void setUp() {
        Mockito.when(zepSoapProvider.createRequestHeaderType()).thenCallRealMethod();
    }


    @Test
    void testGetMonthendReportForUser_MitarbeiterNull() {
        Mockito.when(zepSoapPortType.readMitarbeiter(Mockito.any())).thenReturn(new ReadMitarbeiterResponseType());

        Assertions.assertNull(workerService.getMonthendReportForUser("0"));
    }

    @Test
    void testGetMonthendReportForUser_MitarbeiterInvalid() {
        final MitarbeiterType mitarbeiterType = createMitarbeiter(0, "NULL");
        Mockito.when(zepSoapPortType.readMitarbeiter(Mockito.any())).thenReturn(createReadMitarbeiterResponseType(mitarbeiterType));

        Assertions.assertNull(workerService.getMonthendReportForUser("0"));
    }

    @Test
    void testGetMonthendReportForUser_MitarbeiterValid_ProjektzeitenNull() {
        final MitarbeiterType mitarbeiterType = createMitarbeiter(0, "2020-01-31");
        Mockito.when(zepSoapPortType.readMitarbeiter(Mockito.any())).thenReturn(createReadMitarbeiterResponseType(mitarbeiterType));
        Mockito.when(zepSoapPortType.readProjektzeiten(Mockito.any())).thenReturn(null);

        Assertions.assertNull(workerService.getMonthendReportForUser("0"));
    }


    @Test
    void testGetMonthendReportForUser_MitarbeiterValid_ProjektzeitenInvalid() {
        final MitarbeiterType mitarbeiterType = createMitarbeiter(0, "2020-01-31");
        Mockito.when(zepSoapPortType.readMitarbeiter(Mockito.any())).thenReturn(createReadMitarbeiterResponseType(mitarbeiterType));
        Mockito.when(zepSoapPortType.readProjektzeiten(Mockito.any())).thenReturn(new ReadProjektzeitenResponseType());

        Assertions.assertNull(workerService.getMonthendReportForUser("0"));
    }

    @Test
    void testGetMonthendReportForUser_MitarbeiterValid_ProjektzeitenValid_NoWarning() {
        final MitarbeiterType mitarbeiterType = createMitarbeiter(0, "2020-01-31");
        Mockito.when(zepSoapPortType.readMitarbeiter(Mockito.any())).thenReturn(createReadMitarbeiterResponseType(mitarbeiterType));
        Mockito.when(zepSoapPortType.readProjektzeiten(Mockito.any())).thenReturn(createReadProjektzeitenResponseType("10:00"));

        final MonthlyReport monthendReportForUser = workerService.getMonthendReportForUser("0");
        Assertions.assertNotNull(monthendReportForUser);
        Assertions.assertEquals("Thomas_0@gepardec.com", monthendReportForUser.getMitarbeiter().getEmail());
        Assertions.assertNotNull(monthendReportForUser.getTimeWarnings());
        Assertions.assertTrue(monthendReportForUser.getTimeWarnings().isEmpty());
    }

    @Test
    void testGetMonthendReportForUser_MitarbeiterValid_ProjektzeitenValid_Warning() {
        final MitarbeiterType mitarbeiterType = createMitarbeiter(0, "2020-01-31");
        Mockito.when(zepSoapPortType.readMitarbeiter(Mockito.any())).thenReturn(createReadMitarbeiterResponseType(mitarbeiterType));
        Mockito.when(zepSoapPortType.readProjektzeiten(Mockito.any())).thenReturn(createReadProjektzeitenResponseType("18:00"));

        final MonthlyReport monthendReportForUser = workerService.getMonthendReportForUser("0");
        Assertions.assertNotNull(monthendReportForUser);
        Assertions.assertEquals("Thomas_0@gepardec.com", monthendReportForUser.getMitarbeiter().getEmail());
        Assertions.assertNotNull(monthendReportForUser.getTimeWarnings());
        Assertions.assertFalse(monthendReportForUser.getTimeWarnings().isEmpty());
        Assertions.assertEquals(LocalDate.of(2020, 1, 31), monthendReportForUser.getTimeWarnings().get(0).getDate());
        Assertions.assertEquals(1d, monthendReportForUser.getTimeWarnings().get(0).getExcessWorkTime());
        Assertions.assertEquals(0.5d, monthendReportForUser.getTimeWarnings().get(0).getMissingBreakTime());
    }

    private ReadMitarbeiterResponseType createReadMitarbeiterResponseType(final MitarbeiterType... mitarbeiterType) {
        final MitarbeiterListeType mitarbeiterListeType = new MitarbeiterListeType();
        mitarbeiterListeType.getMitarbeiter().addAll(Arrays.asList(mitarbeiterType));

        final ReadMitarbeiterResponseType readMitarbeiterResponseType = new ReadMitarbeiterResponseType();
        readMitarbeiterResponseType.setMitarbeiterListe(mitarbeiterListeType);

        return readMitarbeiterResponseType;
    }

    private ReadProjektzeitenResponseType createReadProjektzeitenResponseType(String bis) {
        final ProjektzeitType projektzeitType1 = new ProjektzeitType();
        projektzeitType1.setDatum("2020-01-31");
        projektzeitType1.setVon("07:00");
        projektzeitType1.setBis(bis);
        projektzeitType1.setTaetigkeit("BEARBEITEN");
        projektzeitType1.setOrt(WorkingLocation.DEFAULT_WORKING_LOCATION.getWorkingLocationCode());

        final ProjektzeitType projektzeitType2 = new ProjektzeitType();
        projektzeitType2.setDatum("2020-01-30");
        projektzeitType2.setVon("07:00");
        projektzeitType2.setBis("10:00");
        projektzeitType2.setTaetigkeit("BEARBEITEN");
        projektzeitType2.setOrt(WorkingLocation.DEFAULT_WORKING_LOCATION.getWorkingLocationCode());

        final ProjektzeitenListeType projektzeitenListeType = new ProjektzeitenListeType();
        projektzeitenListeType.getProjektzeiten().addAll(Arrays.asList(projektzeitType1, projektzeitType2));

        final ReadProjektzeitenResponseType readProjektzeitenResponseType = new ReadProjektzeitenResponseType();
        readProjektzeitenResponseType.setProjektzeitListe(projektzeitenListeType);

        return readProjektzeitenResponseType;
    }

    private MitarbeiterType createMitarbeiter(final int userId, final String freigabedatum) {
        final MitarbeiterType mitarbeiter = new MitarbeiterType();
        final String name = "Thomas_" + userId;

        mitarbeiter.setEmail(name + "@gepardec.com");
        mitarbeiter.setVorname(name);
        mitarbeiter.setNachname(name + "_Nachname");
        mitarbeiter.setTitel("Ing.");
        mitarbeiter.setUserId(String.valueOf(userId));
        mitarbeiter.setAnrede("Herr");
        mitarbeiter.setPreisgruppe("ARCHITEKT");
        mitarbeiter.setFreigabedatum(freigabedatum);
        mitarbeiter.setRechte(Role.USER.roleId);

        return mitarbeiter;
    }
}
