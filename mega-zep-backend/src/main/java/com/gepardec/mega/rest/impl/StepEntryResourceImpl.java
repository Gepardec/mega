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
import javax.ws.rs.core.Response;
import java.time.LocalDate;

@RequestScoped
@Secured
public class StepEntryResourceImpl implements StepEntryResource {

    @Inject
    StepEntryService stepEntryService;

    @Inject
    UserContext userContext;

    @Override
    public Response close(final EmployeeStepDto employeeStepDto) {
        LocalDate from = LocalDate.parse(DateUtils.getFirstDayOfFollowingMonth(employeeStepDto.currentMonthYear()));
        LocalDate to = LocalDate.parse(DateUtils.getLastDayOfFollowingMonth(employeeStepDto.currentMonthYear()));

        return Response.ok(stepEntryService.setOpenAndAssignedStepEntriesDone(employeeStepDto.employee(), employeeStepDto.stepId(), from, to)).build();
    }

    @Override
    public Response closeForOffice(final EmployeeStepDto employeeStepDto) {
        LocalDate from = LocalDate.parse(DateUtils.getFirstDayOfCurrentMonth(employeeStepDto.currentMonthYear()));
        LocalDate to = LocalDate.parse(DateUtils.getLastDayOfCurrentMonth(employeeStepDto.currentMonthYear()));

        return Response.ok(stepEntryService.setOpenAndAssignedStepEntriesDone(employeeStepDto.employee(), employeeStepDto.stepId(), from, to)).build();
    }

    @Override
    public Response close(final ProjectStepDto projectStepDto) {
        return Response.ok(stepEntryService.closeStepEntryForEmployeeInProject(
                projectStepDto.employee(),
                projectStepDto.stepId(),
                projectStepDto.projectName(),
                userContext.getUser().getEmail(),
                projectStepDto.currentMonthYear()
        )).build();
    }
}
