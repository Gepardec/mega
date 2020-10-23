package com.gepardec.mega.service.impl.stepentry;

import com.gepardec.mega.db.entity.State;
import com.gepardec.mega.db.entity.StepEntry;
import com.gepardec.mega.db.repository.StepEntryRepository;
import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.domain.model.Step;
import com.gepardec.mega.domain.model.User;
import com.gepardec.mega.domain.utils.DateUtils;
import com.gepardec.mega.service.api.stepentry.StepEntryService;

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
    public void addStepEntry(final User owner, final LocalDate date, final String project, final Step step, final User assignee) {
        final LocalDateTime now = LocalDateTime.now();

        final com.gepardec.mega.db.entity.User ownerDb = new com.gepardec.mega.db.entity.User();
        ownerDb.setId(owner.dbId());

        final com.gepardec.mega.db.entity.Step stepDb = new com.gepardec.mega.db.entity.Step();
        stepDb.setId(step.dbId());

        final com.gepardec.mega.db.entity.User assigneeDb = new com.gepardec.mega.db.entity.User();
        assigneeDb.setId(assignee.dbId());

        final StepEntry stepEntry = new StepEntry();
        stepEntry.setCreationDate(now);
        stepEntry.setUpdatedDate(now);
        stepEntry.setDate(date);
        stepEntry.setProject(project);
        stepEntry.setState(State.OPEN);
        stepEntry.setOwner(ownerDb);
        stepEntry.setAssignee(assigneeDb);
        stepEntry.setStep(stepDb);

        stepEntryRepository.persist(stepEntry);
    }
}
