package com.gepardec.mega.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gepardec.mega.db.entity.common.State;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder(builderClassName = "Builder")
@Getter
@ToString
@EqualsAndHashCode
@Accessors(fluent = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Jacksonized
public class EnterpriseEntryDto {

    private final State zepTimesReleased;

    private final State chargeabilityExternalEmployeesRecorded;

    private final State payrollAccountingSent;

    private final State zepMonthlyReportDone;

    private final LocalDate date;

    private final LocalDateTime creationDate;
}
