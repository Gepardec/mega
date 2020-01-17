package com.gepardec.mega.monthlyreport;

import com.gepardec.mega.monthlyreport.journey.JourneyDirection;
import com.gepardec.mega.monthlyreport.journey.JourneyEntry;
import de.provantis.zep.ProjektzeitType;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.gepardec.mega.aplication.utils.DateUtils.parseDateTime;

public class ProjectTimeManager {

    @Getter
    private final Map<LocalDate, List<ProjectTimeEntry>> projectTimes;

    public ProjectTimeManager(List<ProjektzeitType> projectTimes) {

        this.projectTimes = projectTimes.stream()
                .map(ProjectTimeManager::toProjectTimeEntry)
                .sorted(Comparator.comparing(ProjectTimeEntry::getFromTime))
                .collect(Collectors.groupingBy(ProjectTimeEntry::getDate, LinkedHashMap::new, Collectors.toUnmodifiableList()));
    }

    public List<ProjectTimeEntry> getEntriesAsFlatList() {
        return projectTimes.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }


    private static ProjectTimeEntry toProjectTimeEntry(ProjektzeitType projektzeitType) {
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