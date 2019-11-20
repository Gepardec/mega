package com.gepardec.mega.monthendreport;

import de.provantis.zep.ProjektzeitType;
import lombok.Getter;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.gepardec.mega.utils.DateUtils.toDateTime;

public class ProjectTimeManager {

    @Getter
    private final Map<LocalDate, List<ProjectTimeEntry>> projectTimeEntries;

    public ProjectTimeManager(List<ProjektzeitType> projectTimes) {

        projectTimeEntries = projectTimes.stream()
                .map(ProjectTimeManager::toProjectTimeEntry)
                .sorted(Comparator.comparing(ProjectTimeEntry::getFromTime))
                .collect(Collectors.groupingBy(ProjectTimeEntry::getDate, LinkedHashMap::new, Collectors.toUnmodifiableList()));
    }

    public List<ProjectTimeEntry> getEntriesAsList() {
        return projectTimeEntries.values()
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private static ProjectTimeEntry toProjectTimeEntry(ProjektzeitType projektzeitType) {
        return ProjectTimeEntry.of(
                toDateTime(projektzeitType.getDatum(), projektzeitType.getVon()),
                toDateTime(projektzeitType.getDatum(), projektzeitType.getBis()),
                Task.fromString(projektzeitType.getTaetigkeit()).orElse(Task.UNDEFINIERT)
        );
    }

}
