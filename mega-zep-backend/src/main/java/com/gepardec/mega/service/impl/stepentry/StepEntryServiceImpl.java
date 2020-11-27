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
    public boolean setOpenAndAssignedStepEntriesDone(Employee employee, Long stepId) {
        LocalDate fromDate = LocalDate.parse(DateUtils.getFirstDayOfFollowingMonth(employee.releaseDate()));
        LocalDate toDate = LocalDate.parse(DateUtils.getLastDayOfFollowingMonth(employee.releaseDate()));

        return stepEntryRepository.closeAssigned(fromDate, toDate, employee.email(), stepId) > 0;
    }

    @Override
    public boolean closeStepEntryForEmployeeInProject(Employee employee, Long stepId, String project, String assigneeEmail) {
        LocalDate fromDate = LocalDate.parse(DateUtils.getFirstDayOfFollowingMonth(employee.releaseDate()));
        LocalDate toDate = LocalDate.parse(DateUtils.getLastDayOfFollowingMonth(employee.releaseDate()));

        return stepEntryRepository.closeAssigned(fromDate, toDate, employee.email(), assigneeEmail, stepId, project) > 0;
    }

    @Override
    public List<StepEntry> findAllStepEntriesForEmployee(Employee employee) {
        Objects.requireNonNull(employee, "Employee must not be null!");
        LocalDate fromDate = LocalDate.parse(DateUtils.getFirstDayOfFollowingMonth(employee.releaseDate()));
        LocalDate toDate = LocalDate.parse(DateUtils.getLastDayOfFollowingMonth(employee.releaseDate()));

        return stepEntryRepository.findAllOwnedStepEntriesInRange(fromDate, toDate, employee.email());
    }

    @Override
    public List<StepEntry> findAllStepEntriesForEmployeeAndProject(Employee employee, String projectId, String assigneEmail) {
        Objects.requireNonNull(employee, "Employee must not be null!");
        LocalDate fromDate = LocalDate.parse(DateUtils.getFirstDayOfFollowingMonth(employee.releaseDate()));
        LocalDate toDate = LocalDate.parse(DateUtils.getLastDayOfFollowingMonth(employee.releaseDate()));

        return stepEntryRepository.findAllOwnedStepEntriesInRange(fromDate, toDate, employee.email(), projectId, assigneEmail);
    }

    @Override
    public StepEntry findStepEntryForEmployeeAtStep(Long stepId, Employee employee, String assigneeEmail) {
        Objects.requireNonNull(employee, "Employee must not be null!");
        LocalDate fromDate = LocalDate.parse(DateUtils.getFirstDayOfFollowingMonth(employee.releaseDate()));
        LocalDate toDate = LocalDate.parse(DateUtils.getLastDayOfFollowingMonth(employee.releaseDate()));
        Optional<StepEntry> stepEntry = stepEntryRepository.findStepEntryForEmployeeAtStepInRange(
                fromDate, toDate, employee.email(), stepId, assigneeEmail
        );
        if(stepEntry.isEmpty()) {
            throw new IllegalStateException(String.format("No StepEntries found for Employee %s", employee.email()));
        }

        return stepEntry.get();
    }

    @Override
    public StepEntry findStepEntryForEmployeeAndProjectAtStep(Long stepId, Employee employee, String assigneeEmail, String project) {
        Objects.requireNonNull(employee, "Employee must not be null!");
        LocalDate fromDate = LocalDate.parse(DateUtils.getFirstDayOfFollowingMonth(employee.releaseDate()));
        LocalDate toDate = LocalDate.parse(DateUtils.getLastDayOfFollowingMonth(employee.releaseDate()));
        Optional<StepEntry> stepEntry =  stepEntryRepository.findStepEntryForEmployeeAndProjectAtStepInRange(
                fromDate, toDate, employee.email(), stepId, assigneeEmail, project
        );
        if(stepEntry.isEmpty()) {
            throw new IllegalStateException(String.format("No StepEntries found for Employee %s", employee.email()));
        }

        return stepEntry.get();
    }
}
