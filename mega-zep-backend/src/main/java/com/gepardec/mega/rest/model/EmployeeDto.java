package com.gepardec.mega.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeeDto {
    @JsonProperty
    private String userId;

    @JsonProperty
    private String email;

    @JsonProperty
    private String title;

    @JsonProperty
    private String firstname;

    @JsonProperty
    private String lastname;

    @JsonProperty
    private String salutation;

    @JsonProperty
    private String releaseDate;

    @JsonProperty
    private String workDescription;

    @JsonProperty
    private String language;

    @JsonProperty
    private boolean active;
}
