package com.gepardec.mega.domain.model.monthlyreport;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public enum Task {
    BEARBEITEN,
    BESPRECHEN,
    DOKUMENTIEREN,
    REISEN,
    UNDEFINIERT;

    private static final Map<String, Task> tasks = Stream.of(Task.values()).collect(toMap(Task::name, Function.identity()));

    public static boolean isJourney(Task task) {
        return task == REISEN;
    }

    public static boolean isTask(Task task) {
        return !isJourney(task);
    }

    public static Optional<Task> fromString(String name) {
        return Optional.ofNullable(tasks.get(name));
    }

}