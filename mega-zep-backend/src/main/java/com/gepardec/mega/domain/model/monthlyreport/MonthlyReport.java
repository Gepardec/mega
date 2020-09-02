package com.gepardec.mega.domain.model.monthlyreport;

import com.gepardec.mega.db.entity.State;
import com.gepardec.mega.domain.model.Employee;

import java.util.List;

public class MonthlyReport {
    private List<CommentDTO> comments;
    private List<TimeWarning> timeWarnings;
    private List<JourneyWarning> journeyWarnings;
    private Employee employee;
    private State emcState;

    public MonthlyReport() {
    }

    public State getEmcState() {
        return emcState;
    }

    public void setEmcState(State emcState) {
        this.emcState = emcState;
    }

    public List<CommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<CommentDTO> comments) {
        this.comments = comments;
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
