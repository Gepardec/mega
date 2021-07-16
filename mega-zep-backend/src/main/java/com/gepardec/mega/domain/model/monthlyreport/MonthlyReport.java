package com.gepardec.mega.domain.model.monthlyreport;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gepardec.mega.db.entity.employee.EmployeeState;
import com.gepardec.mega.domain.model.Comment;
import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.rest.model.PmProgress;
import com.google.auto.value.AutoValue;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.jackson.Jacksonized;

import javax.annotation.Nullable;
import java.util.List;

@Builder
@Getter
@ToString
@EqualsAndHashCode
@Accessors(fluent = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Jacksonized
public class MonthlyReport {
    @Nullable
    @JsonProperty
    private final Employee employee;

    @Nullable
    @JsonProperty
    private final List<TimeWarning> timeWarnings;

    @Nullable
    @JsonProperty
    private final List<JourneyWarning> journeyWarnings;

    @Nullable
    @JsonProperty
    private final List<Comment> comments;

    @Nullable
    @JsonProperty
    private final EmployeeState employeeCheckState;

    @JsonProperty
    private final boolean isAssigned;

    @JsonProperty
    private final List<PmProgress> employeeProgresses;

    @JsonProperty
    private final boolean otherChecksDone;
}