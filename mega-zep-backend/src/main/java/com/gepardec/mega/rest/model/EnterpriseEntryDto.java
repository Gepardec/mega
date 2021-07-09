package com.gepardec.mega.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gepardec.mega.db.entity.common.State;
import com.gepardec.mega.domain.model.ProjectState;
import com.google.auto.value.AutoValue;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AutoValue
@JsonSerialize(as = EnterpriseEntryDto.class)
@JsonDeserialize(builder = com.gepardec.mega.rest.model.AutoValue_EnterpriseEntryDto.Builder.class)
public abstract class EnterpriseEntryDto {

    public EnterpriseEntryDto() {
        super();
    }

    public static EnterpriseEntryDto.Builder builder() {
        return new com.gepardec.mega.rest.model.AutoValue_EnterpriseEntryDto.Builder();
    }

    @JsonProperty
    public abstract ProjectState zepTimesReleased();

    @JsonProperty
    public abstract ProjectState chargeabilityExternalEmployeesRecorded();

    @JsonProperty
    public abstract ProjectState payrollAccountingSent();

    @JsonProperty
    public abstract ProjectState zepMonthlyReportDone();

    @JsonProperty
    public abstract LocalDate date();

    @JsonProperty
    public abstract LocalDateTime creationDate();

    @AutoValue.Builder
    public abstract static class Builder {
        @JsonProperty
        public abstract Builder zepTimesReleased(ProjectState state);

        @JsonProperty
        public abstract Builder chargeabilityExternalEmployeesRecorded(ProjectState state);

        @JsonProperty
        public abstract Builder payrollAccountingSent(ProjectState state);

        @JsonProperty
        public abstract Builder zepMonthlyReportDone(ProjectState state);

        @JsonProperty
        public abstract Builder date(LocalDate date);

        @JsonProperty
        public abstract Builder creationDate(LocalDateTime creationDate);

        @JsonProperty
        public abstract EnterpriseEntryDto build();
    }
}
