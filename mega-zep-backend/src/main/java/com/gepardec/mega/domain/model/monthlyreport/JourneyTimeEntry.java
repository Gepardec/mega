package com.gepardec.mega.domain.model.monthlyreport;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;

@Builder(builderClassName = "Builder")
@Getter
@ToString
@EqualsAndHashCode
//@Accessors(fluent = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Jacksonized
public class JourneyTimeEntry implements ProjectEntry {
    private final LocalDateTime fromTime;

    private final LocalDateTime toTime;

    private final Task task;

    private final WorkingLocation workingLocation;

    private final JourneyDirection journeyDirection;

    private final Vehicle vehicle;
}