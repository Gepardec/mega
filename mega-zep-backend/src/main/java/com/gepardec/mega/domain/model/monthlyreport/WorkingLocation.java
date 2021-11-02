package com.gepardec.mega.domain.model.monthlyreport;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum WorkingLocation {
    MAIN("- erste Tätigkeitsstätte -"),
    A("A"),
    CZ("CZ"),
    D("D"),
    P("P"),
    CH("Schweiz");

    private static final Map<String, WorkingLocation> zepOrtToWorkingLocation = Stream.of(WorkingLocation.values())
            .collect(Collectors.toMap(WorkingLocation::getZepOrt, Function.identity()));

    public final String zepOrt;

    WorkingLocation(String zepOrt) {
        this.zepOrt = zepOrt;
    }

    public static Optional<WorkingLocation> fromZepOrt(String zepOrt) {
        return Optional.ofNullable(zepOrtToWorkingLocation.get(zepOrt));
    }

    public String getZepOrt() {
        return zepOrt;
    }
}
