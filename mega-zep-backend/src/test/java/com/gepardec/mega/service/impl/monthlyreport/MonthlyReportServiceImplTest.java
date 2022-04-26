package com.gepardec.mega.service.impl.monthlyreport;

import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.domain.model.monthlyreport.MonthlyReport;
import com.gepardec.mega.domain.model.monthlyreport.ProjectEntry;
import com.gepardec.mega.domain.model.monthlyreport.ProjectTimeEntry;
import com.gepardec.mega.domain.model.monthlyreport.Task;
import com.gepardec.mega.domain.model.monthlyreport.TimeWarning;
import com.gepardec.mega.domain.model.monthlyreport.TimeWarningType;
import com.gepardec.mega.domain.model.monthlyreport.WorkingLocation;
import com.gepardec.mega.service.helper.WarningCalculator;
import com.gepardec.mega.service.impl.MonthlyReportServiceImpl;
import com.gepardec.mega.zep.ZepService;
import de.provantis.zep.FehlzeitType;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@QuarkusTest
class MonthlyReportServiceImplTest {

    @InjectMock
    ZepService zepService;

    @InjectMock
    WarningCalculator warningCalculator;

    @Inject
    MonthlyReportServiceImpl monthlyReportService;

    @Test
    void testGetMonthendReportForUser_MitarbeiterValid() {
        final Employee employee = createEmployeeWithReleaseDate(0, "NULL");
        when(zepService.getEmployee(Mockito.anyString())).thenReturn(employee);

        assertThat(monthlyReportService.getMonthendReportForUser("0"))
                .isNotNull();
    }

    @Test
    void testGetMonthendReportForUser_MitarbeiterValid_ProjektzeitenValid() {
        final Employee employee = createEmployee(0);
        when(zepService.getEmployee(Mockito.anyString())).thenReturn(employee);
        when(zepService.getProjectTimes(Mockito.any(Employee.class), Mockito.any(LocalDate.class))).thenReturn(new ArrayList<>());
        when(warningCalculator.determineNoTimeEntries(Mockito.anyList(), Mockito.anyList())).thenReturn(new ArrayList<>());

        assertThat(monthlyReportService.getMonthendReportForUser("0"))
                .isNotNull();
    }

    @Test
    void testGetMonthendReportForUser_MitarbeiterValid_ProjektzeitenValid_NoWarning() {
        final Employee employee = createEmployee(0);
        when(zepService.getEmployee(ArgumentMatchers.anyString())).thenReturn(employee);
        when(zepService.getProjectTimes(Mockito.any(Employee.class), Mockito.any(LocalDate.class))).thenReturn(createReadProjektzeitenResponseType(10));
        when(warningCalculator.determineNoTimeEntries(Mockito.anyList(), Mockito.anyList())).thenReturn(new ArrayList<>());

        final MonthlyReport monthendReportForUser = monthlyReportService.getMonthendReportForUser("0");

        assertThat(monthendReportForUser)
                .isNotNull();
        assertThat(monthendReportForUser.getEmployee().getEmail())
                .isEqualTo("Max_0@gepardec.com");
        assertThat(monthendReportForUser.getTimeWarnings())
                .isNotNull();
        assertThat(monthendReportForUser.getTimeWarnings().isEmpty())
                .isTrue();
    }

    @Test
    void testGetMonthendReportForUser_MitarbeiterValid_ProjektzeitenValid_Warning() {
        final Employee employee = createEmployee(0);
        when(zepService.getEmployee(Mockito.anyString())).thenReturn(employee);
        when(zepService.getProjectTimes(Mockito.any(Employee.class), Mockito.any(LocalDate.class))).thenReturn(createReadProjektzeitenResponseType(18));
        when(zepService.getAbsenceForEmployee(Mockito.any(Employee.class), Mockito.any(LocalDate.class))).thenReturn(new ArrayList<>());
        when(warningCalculator.determineTimeWarnings(Mockito.anyList())).thenReturn(createTimeWarningList());

        final MonthlyReport monthendReportForUser = monthlyReportService.getMonthendReportForUser("0");

        assertThat(monthendReportForUser)
                .isNotNull();
        assertThat(monthendReportForUser.getEmployee().getEmail())
                .isEqualTo("Max_0@gepardec.com");
        assertThat(monthendReportForUser.getTimeWarnings())
                .isNotNull();
        assertThat(Objects.requireNonNull(monthendReportForUser.getTimeWarnings()).isEmpty())
                .isFalse();
        assertThat(monthendReportForUser.getTimeWarnings().get(0).getDate())
                .isEqualTo(LocalDate.of(2020, 1, 31));
        assertThat(monthendReportForUser.getTimeWarnings().get(0).getExcessWorkTime())
                .isEqualTo(1d);
        assertThat(monthendReportForUser.getTimeWarnings().get(0).getMissingBreakTime())
                .isEqualTo(0.5d);
    }

    @Test
    void getMonthendReportForUser_isUserIsValidAndHasNursingAbsenceDays_thenReturnsReportWithCorrectNursingDays() {
        final Employee employee = createEmployee(0);
        when(zepService.getEmployee(Mockito.anyString())).thenReturn(employee);
        when(zepService.getProjectTimes(Mockito.any(Employee.class), Mockito.any(LocalDate.class))).thenReturn(createReadProjektzeitenResponseType(18));
        List<FehlzeitType> absenceList = new ArrayList<>();
        FehlzeitType nursingDay = new FehlzeitType();
        nursingDay.setFehlgrund("PU");
        nursingDay.setGenehmigt(true);
        nursingDay.setEnddatum(LocalDate.of(2020, 2, 29).toString());
        nursingDay.setStartdatum(LocalDate.of(2020, 2, 27).toString());
        absenceList.add(nursingDay);
        when(zepService.getAbsenceForEmployee(Mockito.any(Employee.class), Mockito.any(LocalDate.class))).thenReturn(absenceList);
        when(warningCalculator.determineTimeWarnings(Mockito.anyList())).thenReturn(new ArrayList<>());

        final MonthlyReport monthendReportForUser = monthlyReportService.getMonthendReportForUser("0");

        assertAll(
                () -> assertThat(monthendReportForUser)
                        .isNotNull(),
                () -> assertThat(monthendReportForUser.getEmployee().getEmail())
                        .isEqualTo("Max_0@gepardec.com"),
                () -> assertThat(monthendReportForUser.getTimeWarnings())
                        .isNotNull(),
                () -> assertThat(Objects.requireNonNull(monthendReportForUser.getTimeWarnings()).isEmpty())
                        .isTrue(),
                () -> assertThat(monthendReportForUser.getNursingDays()).isEqualTo(2)
        );
    }

    @Test
    void getMonthendReportForUser_isUserIsValidAndHasMaternityLeaveAbsenceDays_thenReturnsReportWithCorrectAmountOfMaternityLeaveDays() {
        final Employee employee = createEmployee(0);
        when(zepService.getEmployee(Mockito.anyString())).thenReturn(employee);
        when(zepService.getProjectTimes(Mockito.any(Employee.class), Mockito.any(LocalDate.class))).thenReturn(createReadProjektzeitenResponseType(18));
        List<FehlzeitType> absenceList = new ArrayList<>();
        FehlzeitType maternityLeaveDay = new FehlzeitType();
        maternityLeaveDay.setFehlgrund("KA");
        maternityLeaveDay.setGenehmigt(true);
        maternityLeaveDay.setEnddatum(LocalDate.of(2020, 2, 29).toString());
        maternityLeaveDay.setStartdatum(LocalDate.of(2020, 2, 27).toString());
        absenceList.add(maternityLeaveDay);
        when(zepService.getAbsenceForEmployee(Mockito.any(Employee.class), Mockito.any(LocalDate.class))).thenReturn(absenceList);
        when(warningCalculator.determineTimeWarnings(Mockito.anyList())).thenReturn(new ArrayList<>());

        final MonthlyReport monthendReportForUser = monthlyReportService.getMonthendReportForUser("0");

        assertAll(
                () -> assertThat(monthendReportForUser)
                        .isNotNull(),
                () -> assertThat(monthendReportForUser.getEmployee().getEmail())
                        .isEqualTo("Max_0@gepardec.com"),
                () -> assertThat(monthendReportForUser.getTimeWarnings())
                        .isNotNull(),
                () -> assertThat(Objects.requireNonNull(monthendReportForUser.getTimeWarnings()))
                        .isEmpty(),
                () -> assertThat(monthendReportForUser.getMaternityLeaveDays()).isEqualTo(2)
        );
    }

    @Test
    void getMonthendReportForUser_isUserIsValidAndHasExternalTrainingAbsenceDays_thenReturnsReportWithCorrectAmountOfExternalTrainingDays() {
        final Employee employee = createEmployee(0);
        when(zepService.getEmployee(Mockito.anyString())).thenReturn(employee);
        when(zepService.getProjectTimes(Mockito.any(Employee.class), Mockito.any(LocalDate.class))).thenReturn(createReadProjektzeitenResponseType(18));
        List<FehlzeitType> absenceList = new ArrayList<>();
        FehlzeitType externalTrainingAbsence = new FehlzeitType();
        externalTrainingAbsence.setFehlgrund("EW");
        externalTrainingAbsence.setGenehmigt(true);
        externalTrainingAbsence.setEnddatum(LocalDate.of(2020, 2, 29).toString());
        externalTrainingAbsence.setStartdatum(LocalDate.of(2020, 2, 27).toString());
        absenceList.add(externalTrainingAbsence);
        when(zepService.getAbsenceForEmployee(Mockito.any(Employee.class), Mockito.any(LocalDate.class))).thenReturn(absenceList);
        when(warningCalculator.determineTimeWarnings(Mockito.anyList())).thenReturn(new ArrayList<>());

        final MonthlyReport monthendReportForUser = monthlyReportService.getMonthendReportForUser("0");

        assertAll(
                () -> assertThat(monthendReportForUser)
                        .isNotNull(),
                () -> assertThat(monthendReportForUser.getEmployee().getEmail())
                        .isEqualTo("Max_0@gepardec.com"),
                () -> assertThat(monthendReportForUser.getTimeWarnings())
                        .isNotNull(),
                () -> assertThat(Objects.requireNonNull(monthendReportForUser.getTimeWarnings()))
                        .isEmpty(),
                () -> assertThat(monthendReportForUser.getExternalTrainingDays()).isEqualTo(2)
        );
    }

    @Test
    void getMonthendReportForUser_isUserIsValidAndHasConferenceAbsenceDays_thenReturnsReportWithCorrectAmountOfConferenceDays() {
        final Employee employee = createEmployee(0);
        when(zepService.getEmployee(Mockito.anyString())).thenReturn(employee);
        when(zepService.getProjectTimes(Mockito.any(Employee.class), Mockito.any(LocalDate.class))).thenReturn(createReadProjektzeitenResponseType(18));
        List<FehlzeitType> absenceList = new ArrayList<>();
        FehlzeitType conferenceDaysAbsence = new FehlzeitType();
        conferenceDaysAbsence.setFehlgrund("KO");
        conferenceDaysAbsence.setGenehmigt(true);
        conferenceDaysAbsence.setEnddatum(LocalDate.of(2020, 2, 29).toString());
        conferenceDaysAbsence.setStartdatum(LocalDate.of(2020, 2, 27).toString());
        absenceList.add(conferenceDaysAbsence);
        when(zepService.getAbsenceForEmployee(Mockito.any(Employee.class), Mockito.any(LocalDate.class))).thenReturn(absenceList);
        when(warningCalculator.determineTimeWarnings(Mockito.anyList())).thenReturn(new ArrayList<>());

        final MonthlyReport monthendReportForUser = monthlyReportService.getMonthendReportForUser("0");

        assertAll(
                () -> assertThat(monthendReportForUser)
                        .isNotNull(),
                () -> assertThat(monthendReportForUser.getEmployee().getEmail())
                        .isEqualTo("Max_0@gepardec.com"),
                () -> assertThat(monthendReportForUser.getTimeWarnings())
                        .isNotNull(),
                () -> assertThat(Objects.requireNonNull(monthendReportForUser.getTimeWarnings()))
                        .isEmpty(),
                () -> assertThat(monthendReportForUser.getConferenceDays()).isEqualTo(2)
        );
    }

    @Test
    void getMonthendReportForUser_isUserIsValidAndHasMaternityProtectionAbsenceDays_thenReturnsReportWithCorrectAmountOfMaternityProtectionDays() {
        final Employee employee = createEmployee(0);
        when(zepService.getEmployee(Mockito.anyString())).thenReturn(employee);
        when(zepService.getProjectTimes(Mockito.any(Employee.class), Mockito.any(LocalDate.class))).thenReturn(createReadProjektzeitenResponseType(18));
        List<FehlzeitType> absenceList = new ArrayList<>();
        FehlzeitType maternityProtectionDaysAbsence = new FehlzeitType();
        maternityProtectionDaysAbsence.setFehlgrund("MU");
        maternityProtectionDaysAbsence.setGenehmigt(true);
        maternityProtectionDaysAbsence.setEnddatum(LocalDate.of(2020, 2, 29).toString());
        maternityProtectionDaysAbsence.setStartdatum(LocalDate.of(2020, 2, 27).toString());
        absenceList.add(maternityProtectionDaysAbsence);
        when(zepService.getAbsenceForEmployee(Mockito.any(Employee.class), Mockito.any(LocalDate.class))).thenReturn(absenceList);
        when(warningCalculator.determineTimeWarnings(Mockito.anyList())).thenReturn(new ArrayList<>());

        final MonthlyReport monthendReportForUser = monthlyReportService.getMonthendReportForUser("0");

        assertAll(
                () -> assertThat(monthendReportForUser)
                        .isNotNull(),
                () -> assertThat(monthendReportForUser.getEmployee().getEmail())
                        .isEqualTo("Max_0@gepardec.com"),
                () -> assertThat(monthendReportForUser.getTimeWarnings())
                        .isNotNull(),
                () -> assertThat(Objects.requireNonNull(monthendReportForUser.getTimeWarnings()))
                        .isEmpty(),
                () -> assertThat(monthendReportForUser.getMaternityProtectionDays()).isEqualTo(2)
        );
    }

    @Test
    void getMonthendReportForUser_isUserIsValidAndHasFatherMonthAbsenceDays_thenReturnsReportWithCorrectAmountOfFatherMonthDays() {
        final Employee employee = createEmployee(0);
        when(zepService.getEmployee(Mockito.anyString())).thenReturn(employee);
        when(zepService.getProjectTimes(Mockito.any(Employee.class), Mockito.any(LocalDate.class))).thenReturn(createReadProjektzeitenResponseType(18));
        List<FehlzeitType> absenceList = new ArrayList<>();
        FehlzeitType fatherMonthDaysAbsence = new FehlzeitType();
        fatherMonthDaysAbsence.setFehlgrund("PA");
        fatherMonthDaysAbsence.setGenehmigt(true);
        fatherMonthDaysAbsence.setEnddatum(LocalDate.of(2020, 2, 29).toString());
        fatherMonthDaysAbsence.setStartdatum(LocalDate.of(2020, 2, 27).toString());
        absenceList.add(fatherMonthDaysAbsence);
        when(zepService.getAbsenceForEmployee(Mockito.any(Employee.class), Mockito.any(LocalDate.class))).thenReturn(absenceList);
        when(warningCalculator.determineTimeWarnings(Mockito.anyList())).thenReturn(new ArrayList<>());

        final MonthlyReport monthendReportForUser = monthlyReportService.getMonthendReportForUser("0");

        assertAll(
                () -> assertThat(monthendReportForUser)
                        .isNotNull(),
                () -> assertThat(monthendReportForUser.getEmployee().getEmail())
                        .isEqualTo("Max_0@gepardec.com"),
                () -> assertThat(monthendReportForUser.getTimeWarnings())
                        .isNotNull(),
                () -> assertThat(Objects.requireNonNull(monthendReportForUser.getTimeWarnings()))
                        .isEmpty(),
                () -> assertThat(monthendReportForUser.getFatherMonthDays()).isEqualTo(2)
        );
    }

    @Test
    void getMonthendReportForUser_isUserIsValidAndHasPaidSpecialLeaveAbsenceDays_thenReturnsReportWithCorrectAmountOfPaidSpecialLeaveDays() {
        final Employee employee = createEmployee(0);
        when(zepService.getEmployee(Mockito.anyString())).thenReturn(employee);
        when(zepService.getProjectTimes(Mockito.any(Employee.class), Mockito.any(LocalDate.class))).thenReturn(createReadProjektzeitenResponseType(18));
        List<FehlzeitType> absenceList = new ArrayList<>();
        FehlzeitType paidSpecialLeaveDaysAbsence = new FehlzeitType();
        paidSpecialLeaveDaysAbsence.setFehlgrund("SU");
        paidSpecialLeaveDaysAbsence.setGenehmigt(true);
        paidSpecialLeaveDaysAbsence.setEnddatum(LocalDate.of(2020, 2, 29).toString());
        paidSpecialLeaveDaysAbsence.setStartdatum(LocalDate.of(2020, 2, 27).toString());
        absenceList.add(paidSpecialLeaveDaysAbsence);
        when(zepService.getAbsenceForEmployee(Mockito.any(Employee.class), Mockito.any(LocalDate.class))).thenReturn(absenceList);
        when(warningCalculator.determineTimeWarnings(Mockito.anyList())).thenReturn(new ArrayList<>());

        final MonthlyReport monthendReportForUser = monthlyReportService.getMonthendReportForUser("0");

        assertAll(
                () -> assertThat(monthendReportForUser)
                        .isNotNull(),
                () -> assertThat(monthendReportForUser.getEmployee().getEmail())
                        .isEqualTo("Max_0@gepardec.com"),
                () -> assertThat(monthendReportForUser.getTimeWarnings())
                        .isNotNull(),
                () -> assertThat(Objects.requireNonNull(monthendReportForUser.getTimeWarnings()))
                        .isEmpty(),
                () -> assertThat(monthendReportForUser.getPaidSpecialLeaveDays()).isEqualTo(2)
        );
    }

    @Test
    void getMonthendReportForUser_isUserIsValidAndHasNonPaidVacationAbsenceDays_thenReturnsReportWithCorrectAmountOfNonPaidVacationDays() {
        final Employee employee = createEmployee(0);
        when(zepService.getEmployee(Mockito.anyString())).thenReturn(employee);
        when(zepService.getProjectTimes(Mockito.any(Employee.class), Mockito.any(LocalDate.class))).thenReturn(createReadProjektzeitenResponseType(18));
        List<FehlzeitType> absenceList = new ArrayList<>();
        FehlzeitType nonPaidVacationDaysAbsence = new FehlzeitType();
        nonPaidVacationDaysAbsence.setFehlgrund("UU");
        nonPaidVacationDaysAbsence.setGenehmigt(true);
        nonPaidVacationDaysAbsence.setEnddatum(LocalDate.of(2020, 2, 29).toString());
        nonPaidVacationDaysAbsence.setStartdatum(LocalDate.of(2020, 2, 27).toString());
        absenceList.add(nonPaidVacationDaysAbsence);
        when(zepService.getAbsenceForEmployee(Mockito.any(Employee.class), Mockito.any(LocalDate.class))).thenReturn(absenceList);
        when(warningCalculator.determineTimeWarnings(Mockito.anyList())).thenReturn(new ArrayList<>());

        final MonthlyReport monthendReportForUser = monthlyReportService.getMonthendReportForUser("0");

        assertAll(
                () -> assertThat(monthendReportForUser)
                        .isNotNull(),
                () -> assertThat(monthendReportForUser.getEmployee().getEmail())
                        .isEqualTo("Max_0@gepardec.com"),
                () -> assertThat(monthendReportForUser.getTimeWarnings())
                        .isNotNull(),
                () -> assertThat(Objects.requireNonNull(monthendReportForUser.getTimeWarnings()))
                        .isEmpty(),
                () -> assertThat(monthendReportForUser.getNonPaidVacationDays()).isEqualTo(2)
        );
    }

    @Test
    void getMonthendReportForUser_isUserValidAndHasPaidVacationOverWeekend_thenReturnsReportWithOnlyVacationDaysOnWorkdays() {
        final Employee employee = createEmployeeForVacationTests(0);
        when(zepService.getEmployee(Mockito.anyString())).thenReturn(employee);
        when(zepService.getProjectTimes(Mockito.any(Employee.class), Mockito.any(LocalDate.class))).thenReturn(createReadProjectTimesResponseTypeForCorrectVacationDays());
        when(zepService.getAbsenceForEmployee(Mockito.any(Employee.class), Mockito.any(LocalDate.class))).thenReturn(createVacationAbsenceList());
        when(warningCalculator.determineTimeWarnings(Mockito.anyList())).thenReturn(new ArrayList<>());

        final MonthlyReport monthendReportForUser = monthlyReportService.getMonthendReportForUser("0");

        assertAll(
                () -> assertThat(monthendReportForUser)
                        .isNotNull(),
                () -> assertThat(monthendReportForUser.getVacationDays()).isEqualTo(5)
        );
    }

    @Test
    void getMonthendReportForUser_isUserValidAndHasPaidVacationOverWeekendWhichExtendsOverMonthEnd_thenReturnsReportWithOnlyVacationDaysOnWorkdays() {
        final Employee employee = createEmployeeForVacationTests(0);
        when(zepService.getEmployee(Mockito.anyString())).thenReturn(employee);
        when(zepService.getProjectTimes(Mockito.any(Employee.class), Mockito.any(LocalDate.class))).thenReturn(createReadProjectTimesResponseTypeForCorrectVacationDays());
        when(zepService.getAbsenceForEmployee(Mockito.any(Employee.class), Mockito.any(LocalDate.class))).thenReturn(createVacationAbsenceListWhichExtendsOverMonthEnd());
        when(warningCalculator.determineTimeWarnings(Mockito.anyList())).thenReturn(new ArrayList<>());

        final MonthlyReport monthendReportForUser = monthlyReportService.getMonthendReportForUser("0");

        assertAll(
                () -> assertThat(monthendReportForUser)
                        .isNotNull(),
                () -> assertThat(monthendReportForUser.getVacationDays()).isEqualTo(5)
        );
    }

    @Test
    void getMonthendReportForUser_isUserValidAndHasHomeofficeOverWeekend_thenReturnsReportWithHomeOfficeOnWorkdays() {
        final Employee employee = createEmployeeForVacationTests(0);
        when(zepService.getEmployee(Mockito.anyString())).thenReturn(employee);
        when(zepService.getProjectTimes(Mockito.any(Employee.class), Mockito.any(LocalDate.class))).thenReturn(createReadProjectTimesResponseTypeForCorrectVacationDays());
        when(zepService.getAbsenceForEmployee(Mockito.any(Employee.class), Mockito.any(LocalDate.class))).thenReturn(createHomeOfficeListWhichExtendsOverWeekend());
        when(warningCalculator.determineTimeWarnings(Mockito.anyList())).thenReturn(new ArrayList<>());

        final MonthlyReport monthendReportForUser = monthlyReportService.getMonthendReportForUser("0");

        assertAll(
                () -> assertThat(monthendReportForUser)
                        .isNotNull(),
                () -> assertThat(monthendReportForUser.getHomeofficeDays()).isEqualTo(5),
                () -> assertThat(monthendReportForUser.getTimeWarnings()).isEmpty()
        );
    }

    @Test
    void getMonthendReportForUser_isUserValidAndHasHomeofficeOverWeekendAndExtendsOverMonth_thenReturnsReportWithHomeOfficeOnWorkdays() {
        final Employee employee = createEmployeeForVacationTests(0);
        when(zepService.getEmployee(Mockito.anyString())).thenReturn(employee);
        when(zepService.getProjectTimes(Mockito.any(Employee.class), Mockito.any(LocalDate.class))).thenReturn(createReadProjectTimesResponseTypeForCorrectVacationDays());
        when(zepService.getAbsenceForEmployee(Mockito.any(Employee.class), Mockito.any(LocalDate.class))).thenReturn(createHomeOfficeListWhichExtendsOverMonth());
        when(warningCalculator.determineTimeWarnings(Mockito.anyList())).thenReturn(new ArrayList<>());

        final MonthlyReport monthendReportForUser = monthlyReportService.getMonthendReportForUser("0");

        assertAll(
                () -> assertThat(monthendReportForUser)
                        .isNotNull(),
                () -> assertThat(monthendReportForUser.getHomeofficeDays()).isEqualTo(5),
                () -> assertThat(monthendReportForUser.getTimeWarnings()).isEmpty()
        );
    }

    @Test
    void getMonthendReportForUser_isUserValidAndHasTimeCompensationOverWeekend_thenReturnsReportWithCorrectTimeCompensationOnWorkdays() {
        final Employee employee = createEmployeeForVacationTests(0);
        when(zepService.getEmployee(Mockito.anyString())).thenReturn(employee);
        when(zepService.getProjectTimes(Mockito.any(Employee.class), Mockito.any(LocalDate.class))).thenReturn(createReadProjectTimesResponseTypeForCorrectVacationDays());
        when(zepService.getAbsenceForEmployee(Mockito.any(Employee.class), Mockito.any(LocalDate.class))).thenReturn(createTimeCompensationWhichExtendsOverWeekend());
        when(warningCalculator.determineTimeWarnings(Mockito.anyList())).thenReturn(new ArrayList<>());

        final MonthlyReport monthendReportForUser = monthlyReportService.getMonthendReportForUser("0");

        assertAll(
                () -> assertThat(monthendReportForUser)
                        .isNotNull(),
                () -> assertThat(monthendReportForUser.getCompensatoryDays()).isEqualTo(5),
                () -> assertThat(monthendReportForUser.getTimeWarnings()).isEmpty()
        );
    }

    @Test
    void getMonthendReportForUser_isUserValidAndHasTimeCompensationOverWeekendAndExtendsOverMonth_thenReturnsReportWithCorrectTimeCompensationOnWorkdays() {
        final Employee employee = createEmployeeForVacationTests(0);
        when(zepService.getEmployee(Mockito.anyString())).thenReturn(employee);
        when(zepService.getProjectTimes(Mockito.any(Employee.class), Mockito.any(LocalDate.class))).thenReturn(createReadProjectTimesResponseTypeForCorrectVacationDays());
        when(zepService.getAbsenceForEmployee(Mockito.any(Employee.class), Mockito.any(LocalDate.class))).thenReturn(createTimeCompensationWhichExtendsOverWeekendAndMonth());
        when(warningCalculator.determineTimeWarnings(Mockito.anyList())).thenReturn(new ArrayList<>());

        final MonthlyReport monthendReportForUser = monthlyReportService.getMonthendReportForUser("0");

        assertAll(
                () -> assertThat(monthendReportForUser)
                        .isNotNull(),
                () -> assertThat(monthendReportForUser.getCompensatoryDays()).isEqualTo(5),
                () -> assertThat(monthendReportForUser.getTimeWarnings()).isEmpty()
        );
    }

    private List<FehlzeitType> createVacationAbsenceList() {
        List<FehlzeitType> absenceList = new ArrayList<>();

        FehlzeitType vacationDaysAbsence = new FehlzeitType();
        vacationDaysAbsence.setFehlgrund("UB");
        vacationDaysAbsence.setGenehmigt(true);
        vacationDaysAbsence.setEnddatum(LocalDate.of(2022, 4, 29).toString());
        vacationDaysAbsence.setStartdatum(LocalDate.of(2022, 4, 25).toString());
        absenceList.add(vacationDaysAbsence);

        return absenceList;
    }

    private List<FehlzeitType> createHomeOfficeListWhichExtendsOverWeekend() {
        List<FehlzeitType> absenceList = new ArrayList<>();

        FehlzeitType vacationDaysAbsence = new FehlzeitType();
        vacationDaysAbsence.setFehlgrund("HO");
        vacationDaysAbsence.setGenehmigt(true);
        vacationDaysAbsence.setEnddatum(LocalDate.of(2022, 4, 29).toString());
        vacationDaysAbsence.setStartdatum(LocalDate.of(2022, 4, 25).toString());
        absenceList.add(vacationDaysAbsence);

        return absenceList;
    }

    private List<FehlzeitType> createHomeOfficeListWhichExtendsOverMonth() {
        List<FehlzeitType> absenceList = new ArrayList<>();

        FehlzeitType vacationDaysAbsence = new FehlzeitType();
        vacationDaysAbsence.setFehlgrund("HO");
        vacationDaysAbsence.setGenehmigt(true);
        vacationDaysAbsence.setEnddatum(LocalDate.of(2022, 5, 3).toString());
        vacationDaysAbsence.setStartdatum(LocalDate.of(2022, 4, 25).toString());
        absenceList.add(vacationDaysAbsence);

        return absenceList;
    }

    private List<FehlzeitType> createTimeCompensationWhichExtendsOverWeekend() {
        List<FehlzeitType> absenceList = new ArrayList<>();

        FehlzeitType vacationDaysAbsence = new FehlzeitType();
        vacationDaysAbsence.setFehlgrund("FA");
        vacationDaysAbsence.setGenehmigt(true);
        vacationDaysAbsence.setEnddatum(LocalDate.of(2022, 4, 29).toString());
        vacationDaysAbsence.setStartdatum(LocalDate.of(2022, 4, 25).toString());
        absenceList.add(vacationDaysAbsence);

        return absenceList;
    }

    private List<FehlzeitType> createTimeCompensationWhichExtendsOverWeekendAndMonth() {
        List<FehlzeitType> absenceList = new ArrayList<>();

        FehlzeitType vacationDaysAbsence = new FehlzeitType();
        vacationDaysAbsence.setFehlgrund("FA");
        vacationDaysAbsence.setGenehmigt(true);
        vacationDaysAbsence.setEnddatum(LocalDate.of(2022, 5, 3).toString());
        vacationDaysAbsence.setStartdatum(LocalDate.of(2022, 4, 25).toString());
        absenceList.add(vacationDaysAbsence);

        return absenceList;
    }

    private List<FehlzeitType> createVacationAbsenceListWhichExtendsOverMonthEnd() {
        List<FehlzeitType> absenceList = new ArrayList<>();

        FehlzeitType vacationDaysAbsence = new FehlzeitType();
        vacationDaysAbsence.setFehlgrund("UB");
        vacationDaysAbsence.setGenehmigt(true);
        vacationDaysAbsence.setEnddatum(LocalDate.of(2022, 5, 3).toString());
        vacationDaysAbsence.setStartdatum(LocalDate.of(2022, 4, 25).toString());
        absenceList.add(vacationDaysAbsence);

        return absenceList;
    }

    private List<ProjectEntry> createReadProjectTimesResponseTypeForCorrectVacationDays() {
        List<Integer> weekEndDays = List.of(2, 3, 9, 10, 16, 17, 18, 19, 20);
        List<ProjectEntry> projectTimeEntryList = new ArrayList<>();
        for(int i = 1; i <= 22; i++) {
            if(!weekEndDays.contains(i)) {
                projectTimeEntryList.add(ProjectTimeEntry.builder()
                        .fromTime(LocalDateTime.of(2022, 4, i, 8, 0))
                        .toTime(LocalDateTime.of(2022, 4, i, 16, 30))
                        .task(Task.BEARBEITEN)
                        .workingLocation(WorkingLocation.MAIN).build());
            }
        }
        return projectTimeEntryList;
    }

    private List<TimeWarning> createTimeWarningList() {
        TimeWarning timewarning = new TimeWarning();
        timewarning.setDate(LocalDate.of(2020, 1, 31));
        timewarning.getWarningTypes().add(TimeWarningType.OUTSIDE_CORE_WORKING_TIME);
        timewarning.setExcessWorkTime(1d);
        timewarning.setMissingBreakTime(0.5d);
        List<TimeWarning> timeWarningList = new ArrayList<>();
        timeWarningList.add(timewarning);

        return timeWarningList;
    }

    private List<ProjectEntry> createReadProjektzeitenResponseType(int bisHours) {

        return List.of(
                ProjectTimeEntry.builder()
                        .fromTime(LocalDateTime.of(2020, 1, 31, 7, 0))
                        .toTime(LocalDateTime.of(2020, 1, 31, bisHours, 0))
                        .task(Task.BEARBEITEN)
                        .workingLocation(WorkingLocation.MAIN).build(),
                ProjectTimeEntry.builder()
                        .fromTime(LocalDateTime.of(2020, 1, 30, 7, 0))
                        .toTime(LocalDateTime.of(2020, 1, 30, 10, 0))
                        .task(Task.BEARBEITEN)
                        .workingLocation(WorkingLocation.MAIN).build()
        );
    }

    private Employee createEmployee(final int userId) {
        return createEmployeeWithReleaseDate(userId, "2020-01-01");
    }

    private Employee createEmployeeForVacationTests(final int userId) {
        return createEmployeeWithReleaseDate(userId, "2022-03-01");
    }

    private Employee createEmployeeWithReleaseDate(final int userId, String releaseDate) {
        final String name = "Max_" + userId;

        final Employee employee = Employee.builder()
                .email(name + "@gepardec.com")
                .firstname(name)
                .lastname(name + "_Nachname")
                .title("Ing.")
                .userId(String.valueOf(userId))
                .salutation("Herr")
                .workDescription("ARCHITEKT")
                .releaseDate(releaseDate)
                .active(true)
                .build();

        return employee;
    }
}
