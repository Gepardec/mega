package com.gepardec.mega.domain.model;

public enum ProjectState {
    OPEN,
    WORK_IN_PROGRESS,
    DONE,
    NOT_RELEVANT;

    public static ProjectState byName(String name) {
        for (ProjectState projectState : ProjectState.values()) {
            if (projectState.name().equals(name)) {
                return projectState;
            }
        }
        throw new IllegalArgumentException(String.format("No project state for given name '%s' present.", name));
    }

}
