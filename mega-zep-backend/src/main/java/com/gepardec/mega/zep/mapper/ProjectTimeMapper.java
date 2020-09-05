package com.gepardec.mega.zep.mapper;

import com.gepardec.mega.domain.model.monthlyreport.*;
import de.provantis.zep.ProjektzeitType;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.gepardec.mega.domain.utils.DateUtils.parseDateTime;

public class ProjectTimeMapper {

    public static List<ProjectEntry> mapToEntryList(List<ProjektzeitType> projectTimes) {
        return projectTimes.stream()
                .map(ProjectTimeMapper::mapSingleTypeToEntry)
                .sorted(Comparator.comparing(ProjectEntry::getFromTime))
                .collect(Collectors.toList());
    }

    private static ProjectEntry mapSingleTypeToEntry(ProjektzeitType projektzeitType) {
        LocalDateTime fromTime = parseDateTime(projektzeitType.getDatum(), projektzeitType.getVon());
        LocalDateTime toTime = parseDateTime(projektzeitType.getDatum(), projektzeitType.getBis());

        Task task = Task.fromString(projektzeitType.getTaetigkeit())
                .orElseThrow(() -> new IllegalArgumentException("ProjektzeitType.getTaetigkeit could not be converted to Task enum"));

        if (Task.isJourney(task)) {
            JourneyDirection journeyDirection = JourneyDirection.fromString(projektzeitType.getReiseRichtung())
                    .orElseThrow(() -> new IllegalArgumentException("ProjektzeitType.getReiseRichtung could not be converted to JourneyDirection enum"));

            return new JourneyTimeEntry(fromTime, toTime, task, journeyDirection);
        } else {

            return ProjectTimeEntry.of(fromTime, toTime, task);
        }
    }
}