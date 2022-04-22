package com.gepardec.mega.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.jackson.Jacksonized;

@Builder(builderClassName = "Builder")
@Getter
@ToString
@EqualsAndHashCode
@Accessors(fluent = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Jacksonized
public class ConfigDto {
    @JsonProperty
    private final String excelUrl;

    @JsonProperty
    private final String budgetCalculationExcelUrl;

    @JsonProperty
    private final String zepOrigin;

    @JsonProperty
    private final String clientId;

    @JsonProperty
    private final String issuer;

    @JsonProperty
    private final String scope;

    @JsonProperty
    private final String version;
}