package com.gepardec.mega.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gepardec.mega.db.entity.common.State;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@Getter
public class EnterpriseEntryDto {

    private final State zepTimesReleased;

    private final State chargeabilityExternalEmployeesRecorded;

    private final State payrollAccountingSent;

    private final State zepMonthlyReportDone;

    private final LocalDate date;

    private final LocalDateTime creationDate;
}
