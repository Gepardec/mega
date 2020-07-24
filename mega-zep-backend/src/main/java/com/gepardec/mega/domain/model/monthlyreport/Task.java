package com.gepardec.mega.domain.model.monthlyreport;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public enum Task {
    BEARBEITEN,
    BESPRECHEN,
    DOKUMENTIEREN,
    REISEN,
    UNDEFINIERT;

    private static final Map<String, Task> ENUM_MAP = Stream.of(Task.values())
            .collect(toMap(Task::name, task -> task));

    public static boolean isJourney(Task task) {
        return task == REISEN;
    }

    public static boolean isTask(Task task) {
        return !isJourney(task);
    }

    public static Optional<Task> fromString(String name) {
        return Optional.ofNullable(ENUM_MAP.get(StringUtils.defaultIfBlank(name, StringUtils.EMPTY).toUpperCase()));
    }

}