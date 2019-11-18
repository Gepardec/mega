package com.gepardec.mega.monthendreport;

import de.provantis.zep.ProjektzeitType;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.gepardec.mega.utils.DateUtils.toDateTime;

@Data
public class ProjectTimeEntries {

    Map<LocalDate, List<ProjectTimeEntry>> entries = new HashMap<>();

    public ProjectTimeEntries(List<ProjektzeitType> projectTimes) {

        entries = projectTimes.stream()
                .map(projectEntry -> toProjectTimeEntry(projectEntry))
                .sorted((e1, e2) -> e1.getFromTime().compareTo(e2.getFromTime()))
                .collect(Collectors.groupingBy(ProjectTimeEntry::getDate, LinkedHashMap::new, Collectors.toUnmodifiableList()));
    }

    private static ProjectTimeEntry toProjectTimeEntry(ProjektzeitType projektzeitType) {
        return ProjectTimeEntry.of(
                toDateTime(projektzeitType.getDatum(), projektzeitType.getVon()),
                toDateTime(projektzeitType.getDatum(), projektzeitType.getBis()),
                Task.fromString(projektzeitType.getTaetigkeit()).orElse(Task.UNDEFINIERT)
        );
    }
}
