package com.gepardec.mega.service.impl.stepentry;

import com.gepardec.mega.db.entity.State;
import com.gepardec.mega.db.entity.StepEntry;
import com.gepardec.mega.db.repository.StepEntryRepository;
import com.gepardec.mega.db.repository.StepRepository;
import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.domain.utils.DateUtils;
import com.gepardec.mega.service.api.stepentry.StepEntryService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class StepEntryServiceImpl implements StepEntryService {

    @Inject
    StepEntryRepository stepEntryRepository;

    @Inject
    StepRepository stepRepository;

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
    public boolean setOpenAndAssignedStepEntriesDone(Employee employee) {
        LocalDate fromDate = LocalDate.parse(DateUtils.getFirstDayOfFollowingMonth(employee.releaseDate()));
        LocalDate toDate = LocalDate.parse(DateUtils.getLastDayOfFollowingMonth(employee.releaseDate()));

        return stepEntryRepository.setAllOwnedAndAssignedStepEntriesInRangeDone(fromDate, toDate, employee.email()) > 0;
    }
}
