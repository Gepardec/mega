package com.gepardec.mega.rest;

import com.gepardec.mega.application.interceptor.Secured;
import com.gepardec.mega.domain.model.UserContext;
import com.gepardec.mega.domain.utils.DateUtils;
import com.gepardec.mega.rest.model.EmployeeStep;
import com.gepardec.mega.rest.model.ProjectStep;
import com.gepardec.mega.service.api.stepentry.StepEntryService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.time.LocalDate;

@RequestScoped
@Secured
public class StepEntryResource implements StepEntryResourceAPI {

    @Inject
    StepEntryService stepEntryService;

    @Inject
    UserContext userContext;

    @Override
    public boolean close(final EmployeeStep employeeStep) {
        LocalDate from = LocalDate.parse(DateUtils.getFirstDayOfFollowingMonth(employeeStep.currentMonthYear()));
        LocalDate to = LocalDate.parse(DateUtils.getLastDayOfFollowingMonth(employeeStep.currentMonthYear()));

        return stepEntryService.setOpenAndAssignedStepEntriesDone(employeeStep.employee(), employeeStep.stepId(), from, to);
    }

    @Override
    public boolean closeForOffice(final EmployeeStep employeeStep) {
        LocalDate from = LocalDate.parse(DateUtils.getFirstDayOfCurrentMonth(employeeStep.currentMonthYear()));
        LocalDate to = LocalDate.parse(DateUtils.getLastDayOfCurrentMonth(employeeStep.currentMonthYear()));

        return stepEntryService.setOpenAndAssignedStepEntriesDone(employeeStep.employee(), employeeStep.stepId(), from, to);
    }

    @Override
    public boolean close(final ProjectStep projectStep) {
        return stepEntryService.closeStepEntryForEmployeeInProject(
                projectStep.employee(),
                projectStep.stepId(),
                projectStep.projectName(),
                userContext.user().email(),
                projectStep.currentMonthYear()
        );
    }
}
