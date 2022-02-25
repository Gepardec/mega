package com.gepardec.mega.domain.model.monthlyreport;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder(builderClassName = "Builder")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectTimeEntry implements ProjectEntry {

    private LocalDateTime fromTime;

    private LocalDateTime toTime;

    private Task task;

    private WorkingLocation workingLocation;
}