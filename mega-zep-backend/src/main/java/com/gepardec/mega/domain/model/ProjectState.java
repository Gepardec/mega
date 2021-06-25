package com.gepardec.mega.domain.model;

import java.util.Arrays;

public enum ProjectState {
    OPEN,
    WORK_IN_PROGRESS,
    DONE,
    NOT_RELEVANT;

    public static ProjectState byName(String name) {
        return Arrays.stream(ProjectState.values()).filter(v -> v.name().equalsIgnoreCase(name)).
                findFirst().
                orElseThrow(() -> new IllegalArgumentException(String.format("No project state for given name '%s' present.", name)));
    }
}
