package com.gepardec.mega.domain;

import java.util.List;

public class MonthlyReport {

    private List<TimeWarning> timeWarnings;
    private List<JourneyWarning> journeyWarnings;
    private Employee employee;

    public MonthlyReport() {
        // nop
    }

    public List<TimeWarning> getTimeWarnings() {
        return timeWarnings;
    }

    public void setTimeWarnings(List<TimeWarning> timeWarnings) {
        this.timeWarnings = timeWarnings;
    }

    public List<JourneyWarning> getJourneyWarnings() {
        return journeyWarnings;
    }

    public void setJourneyWarnings(List<JourneyWarning> journeyWarnings) {
        this.journeyWarnings = journeyWarnings;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}