package com.gepardec.mega.domain;

import java.time.LocalDateTime;

public class JourneyEntry extends ProjectTimeEntry {

    private JourneyDirection journeyDirection;

    public JourneyEntry(LocalDateTime fromTime, LocalDateTime toTime, Task task, WorkingLocation workingLocation, JourneyDirection journeyDirection) {
        super(fromTime, toTime, task, workingLocation);
        this.journeyDirection = journeyDirection;
    }

    public JourneyDirection getJourneyDirection() {
        return journeyDirection;
    }
}