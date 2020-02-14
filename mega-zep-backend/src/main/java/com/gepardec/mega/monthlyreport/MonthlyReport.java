package com.gepardec.mega.monthlyreport;

import com.gepardec.mega.monthlyreport.journey.JourneyWarning;
import com.gepardec.mega.monthlyreport.warning.TimeWarning;
import com.gepardec.mega.service.model.Employee;

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