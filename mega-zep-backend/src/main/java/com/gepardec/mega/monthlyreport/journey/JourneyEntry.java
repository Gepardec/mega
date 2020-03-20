package com.gepardec.mega.monthlyreport.journey;

import com.gepardec.mega.monthlyreport.ProjectTimeEntry;
import com.gepardec.mega.monthlyreport.Task;
import com.gepardec.mega.monthlyreport.WorkingLocation;

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