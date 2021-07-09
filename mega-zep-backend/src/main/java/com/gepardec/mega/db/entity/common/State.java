package com.gepardec.mega.db.entity.common;

public enum State {
    OPEN(0, "Offen"),
    WORK_IN_PROGRESS(1, "In Arbeit"),
    DONE(2, "Fertig"),
    NOT_RELEVANT(3, "Nicht relevant");

    final int stateId;

    final String stateName;

    State(int stateId, String stateName) {
        this.stateId = stateId;
        this.stateName = stateName;
    }

    public String getStateName() {
        return this.stateName;
    }

    public int getStateId() {
        return this.stateId;
    }
}
