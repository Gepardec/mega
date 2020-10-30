package com.gepardec.mega.domain.model.monthlyreport;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gepardec.mega.db.entity.State;
import com.gepardec.mega.domain.model.Comment;
import com.gepardec.mega.domain.model.Employee;
import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;
import java.util.List;

@AutoValue
@JsonSerialize(as = MonthlyReport.class)
public abstract class MonthlyReport {

    @Nullable
    @JsonProperty
    public abstract Employee employee();

    @Nullable
    @JsonProperty
    public abstract List<TimeWarning> timeWarnings();

    @Nullable
    @JsonProperty
    public abstract List<JourneyWarning> journeyWarnings();

    @JsonCreator
    public static MonthlyReport of(
            @JsonProperty("employee") final Employee employee,
            @JsonProperty("timeWarnings") final List<TimeWarning> timeWarnings,
            @JsonProperty("journeyWarnings") final List<JourneyWarning> journeyWarnings,
            @JsonProperty("comments") final List<Comment> comments,
            @JsonProperty("employeeCheckState") final State employeeCheckState,
            @JsonProperty("isAssigned") final boolean isAssigned,
            @JsonProperty("otherChecksDone") final boolean otherChecksDone) {
        return new com.gepardec.mega.domain.model.monthlyreport.AutoValue_MonthlyReport(
                employee, timeWarnings, journeyWarnings, comments, employeeCheckState, isAssigned, otherChecksDone
        );
    }

    @Nullable
    @JsonProperty
    public abstract List<Comment> comments();

    @Nullable
    @JsonProperty
    public abstract State employeeCheckState();

    @JsonProperty
    public abstract boolean isAssigned();

    @JsonProperty
    public abstract boolean otherChecksDone();
}
