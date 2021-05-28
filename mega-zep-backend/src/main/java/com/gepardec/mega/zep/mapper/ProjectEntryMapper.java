package com.gepardec.mega.zep.mapper;

import com.gepardec.mega.domain.model.monthlyreport.JourneyDirection;
import com.gepardec.mega.domain.model.monthlyreport.JourneyTimeEntry;
import com.gepardec.mega.domain.model.monthlyreport.ProjectEntry;
import com.gepardec.mega.domain.model.monthlyreport.ProjectTimeEntry;
import com.gepardec.mega.domain.model.monthlyreport.Task;
import com.gepardec.mega.domain.model.monthlyreport.Vehicle;
import com.gepardec.mega.domain.model.monthlyreport.WorkingLocation;
import de.provantis.zep.ProjektzeitType;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.gepardec.mega.domain.utils.DateUtils.parseDateTime;

@ApplicationScoped
public class ProjectEntryMapper {

    public List<ProjectEntry> mapList(List<ProjektzeitType> projectTimes) {
        if (projectTimes == null) {
            return null;
        }
        return projectTimes.stream()
                .map(this::map)
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(ProjectEntry::getFromTime))
                .collect(Collectors.toList());
    }

    public ProjectEntry map(ProjektzeitType projektzeitType) {
        if (projektzeitType == null) {
            return null;
        }
        Task task = toTask(projektzeitType.getTaetigkeit());
        LocalDateTime from = toLocalDateTime(projektzeitType.getDatum(), projektzeitType.getVon());
        LocalDateTime to = toLocalDateTime(projektzeitType.getDatum(), projektzeitType.getBis());
        WorkingLocation workingLocation = toWorkingLocation(projektzeitType.getOrt());

        if (Task.isJourney(task)) {
            JourneyDirection journeyDirection = toJourneyDirection(projektzeitType.getReiseRichtung());
            Vehicle vehicle = toVehicle(projektzeitType.getFahrzeug());
            return JourneyTimeEntry.newBuilder()
                    .fromTime(from)
                    .toTime(to)
                    .task(task)
                    .journeyDirection(journeyDirection)
                    .workingLocation(workingLocation)
                    .vehicle(vehicle)
                    .build();
        } else {
            return ProjectTimeEntry.of(from, to, task, workingLocation);
        }
    }

    private WorkingLocation toWorkingLocation(final String ort) {
        return WorkingLocation.fromZepOrt(ort)
                .orElseThrow(() -> new IllegalArgumentException("Arbeitsort '" + ort + "' is not mapped in Enum WorkingLocation"));
    }

    private LocalDateTime toLocalDateTime(String datum, String von) {
        return parseDateTime(datum, von);
    }

    private Task toTask(String taetigkeit) {
        return Task.fromString(taetigkeit)
                .orElseThrow(() -> new IllegalArgumentException("Tätigkeit '" + taetigkeit + "' is not mapped in Enum Task"));
    }

    private Vehicle toVehicle(final String vehicle) {
        return Vehicle.forId(vehicle)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle '" + vehicle + "' is not mapped in Enum Vehicle"));
    }

    private JourneyDirection toJourneyDirection(final String direction) {
        return JourneyDirection.fromString(direction)
                .orElseThrow(() -> new IllegalArgumentException("JourneyDirection '" + direction + "' is not mapped in Enum JourneyDirection"));
    }
}
