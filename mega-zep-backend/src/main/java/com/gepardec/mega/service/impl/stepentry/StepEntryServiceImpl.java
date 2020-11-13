package com.gepardec.mega.service.impl.stepentry;

import com.gepardec.mega.db.entity.State;
import com.gepardec.mega.db.entity.StepEntry;
import com.gepardec.mega.db.repository.StepEntryRepository;
import com.gepardec.mega.db.repository.StepRepository;
import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.domain.model.Project;
import com.gepardec.mega.domain.model.Step;
import com.gepardec.mega.domain.model.User;
import com.gepardec.mega.domain.utils.DateUtils;
import com.gepardec.mega.service.api.stepentry.StepEntryService;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class StepEntryServiceImpl implements StepEntryService {

    @Inject
    Logger logger;

    @Inject
    StepEntryRepository stepEntryRepository;

    @Override
    public Optional<State> findEmployeeCheckState(final Employee employee) {
        LocalDate fromDate = LocalDate.parse(DateUtils.getFirstDayOfFollowingMonth(employee.releaseDate()));
        LocalDate toDate = LocalDate.parse(DateUtils.getLastDayOfFollowingMonth(employee.releaseDate()));

        List<StepEntry> stepEntries =
                stepEntryRepository.findAllOwnedAndAssignedStepEntriesInRange(fromDate, toDate, employee.email());

        List<State> states = stepEntries.stream().map(StepEntry::getState).collect(Collectors.toList());

        return states.isEmpty() ? Optional.empty() : Optional.ofNullable(states.get(0));
    }

    @Override
    public boolean areOtherChecksDone(Employee employee) {
        LocalDate fromDate = LocalDate.parse(DateUtils.getFirstDayOfFollowingMonth(employee.releaseDate()));
        LocalDate toDate = LocalDate.parse(DateUtils.getLastDayOfFollowingMonth(employee.releaseDate()));

        List<StepEntry> stepEntries =
                stepEntryRepository.findAllOwnedAndUnassignedStepEntriesInRange(fromDate, toDate, employee.email());

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
}
