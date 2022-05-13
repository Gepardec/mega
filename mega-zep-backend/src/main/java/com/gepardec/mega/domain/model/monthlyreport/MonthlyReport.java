package com.gepardec.mega.domain.model.monthlyreport;

import com.gepardec.mega.db.entity.employee.EmployeeState;
import com.gepardec.mega.domain.model.Comment;
import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.rest.model.PmProgressDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyReport {
    private Employee employee;

    private List<TimeWarning> timeWarnings;

    private List<JourneyWarning> journeyWarnings;

    private List<Comment> comments;

    private EmployeeState employeeCheckState;

    private boolean isAssigned;

    private List<PmProgressDto> employeeProgresses;

    private boolean otherChecksDone;

    private int vacationDays;

    private int homeofficeDays;

    private int compensatoryDays;

    private int nursingDays;

    private int maternityLeaveDays;

    private int externalTrainingDays;

    private int conferenceDays;

    private int maternityProtectionDays;

    private int fatherMonthDays;

    private int paidSpecialLeaveDays;

    private int nonPaidVacationDays;

    private String billableTime;

    private String totalWorkingTime;
}