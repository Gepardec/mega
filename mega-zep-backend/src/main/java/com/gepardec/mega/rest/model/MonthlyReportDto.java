package com.gepardec.mega.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gepardec.mega.db.entity.employee.EmployeeState;
import com.gepardec.mega.domain.model.Comment;
import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.domain.model.monthlyreport.JourneyWarning;
import com.gepardec.mega.domain.model.monthlyreport.TimeWarning;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Jacksonized
@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MonthlyReportDto {
    @JsonProperty
    private Employee employee;

    @JsonProperty
    private List<TimeWarning> timeWarnings;

    @JsonProperty
    private List<JourneyWarning> journeyWarnings;

    @JsonProperty
    private List<Comment> comments;

    @JsonProperty
    private EmployeeState employeeCheckState;

    @JsonProperty
    private boolean isAssigned;

    @JsonProperty
    private List<PmProgressDto> employeeProgresses;

    @JsonProperty
    private boolean otherChecksDone;

    @JsonProperty
    private int vacationDays;

    @JsonProperty
    private int homeofficeDays;

    @JsonProperty
    private int compensatoryDays;

    @JsonProperty
    private String billableTime;

    @JsonProperty
    private String totalWorkingTime;
}