package com.gepardec.mega.domain.model.monthlyreport;

import java.time.LocalDateTime;

public class JourneyEntry extends ProjectTimeEntry {

    private JourneyDirection journeyDirection;

    public JourneyEntry(LocalDateTime fromTime, LocalDateTime toTime, Task task, JourneyDirection journeyDirection) {
        super(fromTime, toTime, task);
        this.journeyDirection = journeyDirection;
    }

    public JourneyDirection getJourneyDirection() {
        return journeyDirection;
    }
}