package com.gepardec.mega.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gepardec.mega.domain.model.ProjectState;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder(builderClassName = "Builder")
@Getter
@ToString
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@Jacksonized
public class EnterpriseEntryDto {

    private final ProjectState zepTimesReleased;

    private final ProjectState chargeabilityExternalEmployeesRecorded;

    private final ProjectState payrollAccountingSent;

    private final ProjectState zepMonthlyReportDone;

    private final LocalDate date;

    private final LocalDateTime creationDate;
}
