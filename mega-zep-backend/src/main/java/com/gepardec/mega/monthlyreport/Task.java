package com.gepardec.mega.monthlyreport;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public enum Task {
    BEARBEITEN,
    BESPRECHEN,
    DOKUMENTIEREN,
    REISEN,
    UNDEFINIERT;

    private static Map<String, Task> enumMap = Stream.of(Task.values())
            .collect(toMap(Task::name, task -> task));

    public static boolean isJourney(Task task) {
        return task == REISEN;
    }

    public static Task fromString(String name) {

        Task task = enumMap.get(StringUtils.defaultIfBlank(name, StringUtils.EMPTY).toUpperCase());
        if (task == null) {
            throw new EnumConverterException(String.format("Error mapping %s to Enum Task", name));
        }
        return task;
    }
}