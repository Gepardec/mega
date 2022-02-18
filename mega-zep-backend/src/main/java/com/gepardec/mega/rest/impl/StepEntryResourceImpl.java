package com.gepardec.mega.rest.impl;

import com.gepardec.mega.application.interceptor.Secured;
import com.gepardec.mega.domain.model.UserContext;
import com.gepardec.mega.domain.utils.DateUtils;
import com.gepardec.mega.rest.api.StepEntryResource;
import com.gepardec.mega.rest.model.EmployeeStepDto;
import com.gepardec.mega.rest.model.ProjectStepDto;
import com.gepardec.mega.service.api.StepEntryService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.time.LocalDate;

@RequestScoped
@Secured
public class StepEntryResourceImpl implements StepEntryResource {

    @Inject
    StepEntryService stepEntryService;

    @Inject
    UserContext userContext;

    @Override
    public boolean close(final EmployeeStepDto employeeStepDto) {
        LocalDate from = LocalDate.parse(DateUtils.getFirstDayOfFollowingMonth(employeeStepDto.currentMonthYear()));
        LocalDate to = LocalDate.parse(DateUtils.getLastDayOfFollowingMonth(employeeStepDto.currentMonthYear()));

        return stepEntryService.setOpenAndAssignedStepEntriesDone(employeeStepDto.employee(), employeeStepDto.stepId(), from, to);
    }

    @Override
    public boolean closeForOffice(final EmployeeStepDto employeeStepDto) {
        LocalDate from = LocalDate.parse(DateUtils.getFirstDayOfCurrentMonth(employeeStepDto.currentMonthYear()));
        LocalDate to = LocalDate.parse(DateUtils.getLastDayOfCurrentMonth(employeeStepDto.currentMonthYear()));

        return stepEntryService.setOpenAndAssignedStepEntriesDone(employeeStepDto.employee(), employeeStepDto.stepId(), from, to);
    }

    @Override
    public boolean close(final ProjectStepDto projectStepDto) {
        return stepEntryService.closeStepEntryForEmployeeInProject(
                projectStepDto.employee(),
                projectStepDto.stepId(),
                projectStepDto.projectName(),
                userContext.user().email(),
                projectStepDto.currentMonthYear()
        );
    }
}
