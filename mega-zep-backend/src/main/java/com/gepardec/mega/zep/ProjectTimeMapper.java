package com.gepardec.mega.zep;

import com.gepardec.mega.domain.model.*;
import de.provantis.zep.ProjektzeitType;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.gepardec.mega.domain.utils.DateUtils.parseDateTime;

public class ProjectTimeMapper {

    public static List<ProjectTimeEntry> mapToEntryList(List<ProjektzeitType> projectTimes) {
        return projectTimes.stream()
                .map(ProjectTimeMapper::mapSingleTypeToEntry)
                .sorted(Comparator.comparing(ProjectTimeEntry::getFromTime))
                .collect(Collectors.toList());
    }

    private static ProjectTimeEntry mapSingleTypeToEntry(ProjektzeitType projektzeitType) {
        LocalDateTime fromTime = parseDateTime(projektzeitType.getDatum(), projektzeitType.getVon());
        LocalDateTime toTime = parseDateTime(projektzeitType.getDatum(), projektzeitType.getBis());
        Task task = Task.fromString(projektzeitType.getTaetigkeit())
                .orElseThrow(() -> new IllegalArgumentException("ProjektzeitType.getTaetigkeit could not be converted to Task enum"));
        WorkingLocation workingLocation = WorkingLocation.fromString(projektzeitType.getOrt())
                .orElseThrow(() -> new IllegalArgumentException("ProjektzeitType.getOrt could not be converted to WorkingLocation enum"));

        if (Task.isJourney(task)) {
            JourneyDirection journeyDirection = JourneyDirection.fromString(projektzeitType.getReiseRichtung())
                    .orElseThrow(() -> new IllegalArgumentException("ProjektzeitType.getReiseRichtung could not be converted to JourneyDirection enum"));
            return new JourneyEntry(fromTime, toTime, task, workingLocation, journeyDirection);
        } else {
            return new ProjectTimeEntry(fromTime, toTime, task, workingLocation);
        }
    }
}