package com.gepardec.mega.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gepardec.mega.db.entity.employee.EmployeeState;
import com.gepardec.mega.db.entity.employee.StepEntry;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.annotation.Nullable;

@Jacksonized
@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PmProgressDto {
    @JsonProperty
    @Nullable
    private String project;

    @JsonProperty
    private String assigneeEmail;

    @JsonProperty
    @Nullable
    private String firstname;

    @JsonProperty
    @Nullable
    private String lastname;

    @JsonProperty
    private EmployeeState state;

    @JsonProperty
    private Long stepId;

    public static PmProgressDto ofStepEntry(StepEntry stepEntry) {
        return PmProgressDto.builder()
                .project(stepEntry.getProject())
                .assigneeEmail(stepEntry.getAssignee().getEmail())
                .firstname(stepEntry.getAssignee().getFirstname())
                .lastname(stepEntry.getAssignee().getLastname())
                .state(stepEntry.getState())
                .stepId(stepEntry.getStep().getId())
                .build();
    }
}
