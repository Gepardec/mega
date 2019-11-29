package com.gepardec.mega.monthlyreport;

import lombok.AccessLevel;
import lombok.Getter;

import java.time.LocalDateTime;

public class JourneyEntry extends ProjectTimeEntry {
    @Getter(AccessLevel.PACKAGE)
    private JourneyDirection journeyDirection;

    public JourneyEntry(LocalDateTime fromTime, LocalDateTime toTime, Task task, WorkingLocation workingLocation, JourneyDirection journeyDirection) {
        super(fromTime, toTime, task, workingLocation);
        this.journeyDirection = journeyDirection;
    }

}