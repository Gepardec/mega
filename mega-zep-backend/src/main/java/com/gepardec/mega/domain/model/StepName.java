package com.gepardec.mega.domain.model;

public enum StepName {
    CONTROL_TIMES(1L),
    CONTROL_INTERNAL_TIMES(2L),
    CONTROL_EXTERNAL_TIMES(3L),
    CONTROL_TIME_EVIDENCES(4L),
    ACCEPT_TIMES(5L);

    private final long id;

    StepName(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
