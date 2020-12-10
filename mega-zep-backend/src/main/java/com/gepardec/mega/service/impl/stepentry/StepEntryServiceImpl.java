package com.gepardec.mega.service.impl.stepentry;

import com.gepardec.mega.db.entity.State;
import com.gepardec.mega.db.entity.StepEntry;
import com.gepardec.mega.db.repository.StepEntryRepository;
import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.domain.utils.DateUtils;
import com.gepardec.mega.service.api.stepentry.StepEntryService;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@ApplicationScoped
public class StepEntryServiceImpl implements StepEntryService {

    @Inject
    Logger logger;

    @Inject
    StepEntryRepository stepEntryRepository;

    @Override
    public Optional<State> findEmployeeCheckState(final Employee employee) {
        LocalDate entryDate = LocalDate.parse(DateUtils.getFirstDayOfFollowingMonth(employee.releaseDate()));

        Optional<StepEntry> stepEntries =
                stepEntryRepository.findAllOwnedAndAssignedStepEntriesForEmployee(entryDate, employee.email());

        return stepEntries.map(StepEntry::getState);
    }

    @Override
    public boolean areOtherChecksDone(Employee employee) {
        LocalDate entryDate = LocalDate.parse(DateUtils.getFirstDayOfFollowingMonth(employee.releaseDate()));

        List<StepEntry> stepEntries =
                stepEntryRepository.findAllOwnedAndUnassignedStepEntriesForOtherChecks(entryDate, employee.email());

        return stepEntries.stream().allMatch(stepEntry -> stepEntry.getState() == State.DONE);
    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public void addStepEntry(com.gepardec.mega.domain.model.StepEntry stepEntry) {
        final com.gepardec.mega.db.entity.User ownerDb = new com.gepardec.mega.db.entity.User();
        ownerDb.setId(stepEntry.owner().dbId());

        final com.gepardec.mega.db.entity.Step stepDb = new com.gepardec.mega.db.entity.Step();
        stepDb.setId(stepEntry.step().dbId());

        final com.gepardec.mega.db.entity.User assigneeDb = new com.gepardec.mega.db.entity.User();
        assigneeDb.setId(stepEntry.assignee().dbId());

        final StepEntry stepEntryDb = new StepEntry();
        stepEntryDb.setDate(stepEntry.date());
        stepEntryDb.setProject(stepEntry.project() != null ? stepEntry.project().projectId() : null);
        stepEntryDb.setState(State.OPEN);
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
        if(stepEntry.isEmpty()) {
            throw new IllegalStateException(String.format("No StepEntries found for Employee %s", employee.email()));
        }

        return stepEntry.get();
    }

    @Override
    public StepEntry findStepEntryForEmployeeAndProjectAtStep(Long stepId, Employee employee, String assigneeEmail, String project, String currentMonthYear) {
        Objects.requireNonNull(employee, "Employee must not be null!");
        LocalDate fromDate = LocalDate.parse(DateUtils.getFirstDayOfCurrentMonth(currentMonthYear));
        LocalDate toDate = LocalDate.parse(DateUtils.getLastDayOfCurrentMonth(currentMonthYear));
        Optional<StepEntry> stepEntry =  stepEntryRepository.findStepEntryForEmployeeAndProjectAtStepInRange(
                fromDate, toDate, employee.email(), stepId, assigneeEmail, project
        );
        if(stepEntry.isEmpty()) {
            throw new IllegalStateException(String.format("No StepEntries found for Employee %s", employee.email()));
        }

        return stepEntry.get();
    }
}
