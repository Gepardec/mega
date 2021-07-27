package com.gepardec.mega.service.impl.stepentry;

import com.gepardec.mega.db.entity.employee.EmployeeState;
import com.gepardec.mega.db.entity.employee.Step;
import com.gepardec.mega.db.entity.employee.StepEntry;
import com.gepardec.mega.db.entity.employee.User;
import com.gepardec.mega.db.repository.StepEntryRepository;
import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.domain.model.ProjectEmployees;
import com.gepardec.mega.domain.utils.DateUtils;
import com.gepardec.mega.service.api.stepentry.StepEntryService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class StepEntryServiceImpl implements StepEntryService {

    @Inject
    Logger logger;

    @Inject
    StepEntryRepository stepEntryRepository;

    @Override
    public Optional<EmployeeState> findEmployeeCheckState(final Employee employee) {
        LocalDate entryDate = LocalDate.parse(DateUtils.getFirstDayOfFollowingMonth(employee.releaseDate()));

        Optional<StepEntry> stepEntries =
                stepEntryRepository.findAllOwnedAndAssignedStepEntriesForEmployee(entryDate, employee.email());

        return stepEntries.map(StepEntry::getState);
    }

    @Override
    public List<StepEntry> findAllOwnedAndUnassignedStepEntriesForOtherChecks(Employee employee) {
        LocalDate entryDate = parseReleaseDate(employee.releaseDate());
        return stepEntryRepository.findAllOwnedAndUnassignedStepEntriesForOtherChecks(entryDate, employee.email());
    }

    @Override
    public List<StepEntry> findAllOwnedAndUnassignedStepEntriesForPMProgress(String email, String date) {
        LocalDate entryDate = parseReleaseDate(date);

        return stepEntryRepository.findAllOwnedAndUnassignedStepEntriesForPMProgress(entryDate, email);
    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public void addStepEntry(com.gepardec.mega.domain.model.StepEntry stepEntry) {
        final User ownerDb = new User();
        ownerDb.setId(stepEntry.owner().dbId());

        final Step stepDb = new Step();
        stepDb.setId(stepEntry.step().dbId());

        final User assigneeDb = new User();
        assigneeDb.setId(stepEntry.assignee().dbId());

        final StepEntry stepEntryDb = new StepEntry();
        stepEntryDb.setDate(stepEntry.date());
        stepEntryDb.setProject(stepEntry.project() != null ? stepEntry.project().projectId() : null);
        stepEntryDb.setState(EmployeeState.OPEN);
        stepEntryDb.setOwner(ownerDb);
        stepEntryDb.setAssignee(assigneeDb);
        stepEntryDb.setStep(stepDb);

        logger.debug("inserting step entry {}", stepEntryDb);

        stepEntryRepository.persist(stepEntryDb);
    }

    @Override
    public boolean setOpenAndAssignedStepEntriesDone(Employee employee, Long stepId, LocalDate from, LocalDate to) {
        return stepEntryRepository.closeAssigned(from, to, employee.email(), stepId) > 0;
    }

    @Override
    public boolean closeStepEntryForEmployeeInProject(Employee employee, Long stepId, String project, String assigneeEmail, String currentMonthYear) {
        LocalDate fromDate = LocalDate.parse(DateUtils.getFirstDayOfCurrentMonth(currentMonthYear));
        LocalDate toDate = LocalDate.parse(DateUtils.getLastDayOfCurrentMonth(currentMonthYear));

        return stepEntryRepository.closeAssigned(fromDate, toDate, employee.email(), assigneeEmail, stepId, project) > 0;
    }

    @Override
    public List<StepEntry> findAllStepEntriesForEmployee(Employee employee, LocalDate from, LocalDate to) {
        Objects.requireNonNull(employee, "Employee must not be null!");
        return stepEntryRepository.findAllOwnedStepEntriesInRange(from, to, employee.email());
    }

    @Override
    public List<StepEntry> findAllStepEntriesForEmployeeAndProject(Employee employee, String projectId, String assigneEmail,
                                                                   LocalDate from, LocalDate to) {
        Objects.requireNonNull(employee, "Employee must not be null!");

        List<StepEntry> stepEntries = new ArrayList<>();
        stepEntries.addAll(stepEntryRepository.findAllOwnedStepEntriesInRange(from, to, employee.email(), projectId, assigneEmail));
        stepEntries.addAll(stepEntryRepository.findAllOwnedStepEntriesInRange(from, to, employee.email()));
        return stepEntries;
    }

    @Override
    public StepEntry findStepEntryForEmployeeAtStep(Long stepId, Employee employee, String assigneeEmail, String currentMonthYear) {
        Objects.requireNonNull(employee, "Employee must not be null!");
        LocalDate fromDate = LocalDate.parse(DateUtils.getFirstDayOfCurrentMonth(currentMonthYear));
        LocalDate toDate = LocalDate.parse(DateUtils.getLastDayOfCurrentMonth(currentMonthYear));
        Optional<StepEntry> stepEntry = stepEntryRepository.findStepEntryForEmployeeAtStepInRange(
                fromDate, toDate, employee.email(), stepId, assigneeEmail
        );
        if (stepEntry.isEmpty()) {
            throw new IllegalStateException(String.format("No StepEntries found for Employee %s", employee.email()));
        }

        return stepEntry.get();
    }

    @Override
    public StepEntry findStepEntryForEmployeeAndProjectAtStep(Long stepId, Employee employee, String assigneeEmail, String project, String currentMonthYear) {
        Objects.requireNonNull(employee, "Employee must not be null!");
        LocalDate fromDate = LocalDate.parse(DateUtils.getFirstDayOfCurrentMonth(currentMonthYear));
        LocalDate toDate = LocalDate.parse(DateUtils.getLastDayOfCurrentMonth(currentMonthYear));
        Optional<StepEntry> stepEntry = stepEntryRepository.findStepEntryForEmployeeAndProjectAtStepInRange(
                fromDate, toDate, employee.email(), stepId, assigneeEmail, project
        );
        if (stepEntry.isEmpty()) {
            throw new IllegalStateException(String.format("No StepEntries found for Employee %s", employee.email()));
        }

        return stepEntry.get();
    }

    @Override
    public List<ProjectEmployees> getProjectEmployeesForPM(final LocalDate from, final LocalDate to, String assigneEmail) {
        return stepEntryRepository.findAllStepEntriesForPMInRange(from, to, assigneEmail)
                .stream()
                .collect(Collectors.groupingBy(StepEntry::getProject, Collectors.mapping(s -> s.getOwner().getZepId(), Collectors.toList())))
                .entrySet()
                .stream()
                .map(e -> ProjectEmployees.builder().projectId(e.getKey()).employees(e.getValue()).build())
                .collect(Collectors.toList());
    }

    @Override
    public List<ProjectEmployees> getAllProjectEmployeesForPM(LocalDate from, LocalDate to) {
        return stepEntryRepository.findAllStepEntriesForAllPMInRange(from, to)
                .stream()
                .collect(Collectors.groupingBy(StepEntry::getProject, Collectors.mapping(s -> s.getOwner().getZepId(), Collectors.toList())))
                .entrySet()
                .stream()
                .map(e -> ProjectEmployees.builder().projectId(e.getKey()).employees(e.getValue()).build())
                .collect(Collectors.toList());
    }

    private LocalDate parseReleaseDate(String releaseDate) {
        LocalDate entryDate;
        if (StringUtils.isBlank(releaseDate) || StringUtils.equalsIgnoreCase(releaseDate, "NULL")) {
            entryDate = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
        } else {
            entryDate = LocalDate.parse(DateUtils.getFirstDayOfFollowingMonth(releaseDate));
        }
        return entryDate;
    }
}
