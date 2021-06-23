package com.gepardec.mega.zep.mapper;

import com.gepardec.mega.domain.model.monthlyreport.JourneyDirection;
import com.gepardec.mega.domain.model.monthlyreport.JourneyTimeEntry;
import com.gepardec.mega.domain.model.monthlyreport.ProjectEntry;
import com.gepardec.mega.domain.model.monthlyreport.ProjectTimeEntry;
import com.gepardec.mega.domain.model.monthlyreport.Task;
import com.gepardec.mega.domain.model.monthlyreport.Vehicle;
import com.gepardec.mega.domain.model.monthlyreport.WorkingLocation;
import de.provantis.zep.ProjektzeitType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProjectEntryMapperTest {

    private ProjectEntryMapper mapper;

    @BeforeEach
    void beforeEach() {
        mapper = new ProjectEntryMapper();
    }

    private ProjektzeitType pojektzeitTypeFor(String taetigkeit) {
        return projektzeitTypeFor(taetigkeit, WorkingLocation.MAIN.zepOrt, null, Vehicle.OTHER_INACTIVE.id);
    }

    private ProjektzeitType projektzeitTypeFor(String taetigkeit, String ort, String reiseRichtung, String fahrzeug) {
        ProjektzeitType projektzeitType = new ProjektzeitType();
        projektzeitType.setDatum("2020-07-01");
        projektzeitType.setVon("08:00");
        projektzeitType.setBis("10:00");
        projektzeitType.setTaetigkeit(taetigkeit);
        projektzeitType.setOrt(ort);
        projektzeitType.setFahrzeug(fahrzeug);
        projektzeitType.setReiseRichtung(reiseRichtung);

        return projektzeitType;
    }

    @Test
    void whenNull_thenReturnsNull() {
        assertNull(mapper.map(null));
    }

    @Test
    void withTaetigkeitIsNotATask_thenThrowsIllegalArgumentException() {
        ProjektzeitType projektzeitType = projektzeitTypeFor("testen", WorkingLocation.MAIN.zepOrt, JourneyDirection.TO.getDirection(),
                Vehicle.OTHER_INACTIVE.id);

        assertThrows(IllegalArgumentException.class, () -> mapper.map(projektzeitType));
    }

    @Test
    void withOrtIsNotAWorkingLocation_thenThrowsIllegalArgumentException() {
        ProjektzeitType projektzeitType = projektzeitTypeFor(Task.REISEN.name(), "unbekannt", JourneyDirection.TO.getDirection(), Vehicle.OTHER_INACTIVE.id);

        assertThrows(IllegalArgumentException.class, () -> mapper.map(projektzeitType));
    }

    @Test
    void withReiseRichtungIsNotAJourneyDirection_thenThrowsIllegalArgumentException() {
        ProjektzeitType projektzeitType = projektzeitTypeFor(Task.REISEN.name(), WorkingLocation.MAIN.zepOrt, "100", Vehicle.OTHER_INACTIVE.id);

        assertThrows(IllegalArgumentException.class, () -> mapper.map(projektzeitType));
    }

    @Test
    void withFahrzeugIsNotAVehicle_thenThrowsIllegalArgumentException() {
        ProjektzeitType projektzeitType = projektzeitTypeFor(Task.REISEN.name(), WorkingLocation.MAIN.zepOrt, JourneyDirection.TO.getDirection(),
                "UNKOWN");

        assertThrows(IllegalArgumentException.class, () -> mapper.map(projektzeitType));
    }

    @Test
    void withTaskIsBearbeiten_thenReturnsProjectTimeEntry() {
        ProjektzeitType projektzeitType = pojektzeitTypeFor(Task.BEARBEITEN.name());

        ProjectEntry actual = mapper.map(projektzeitType);

        assertTrue(actual instanceof ProjectTimeEntry);
    }

    @Test
    void withTaskIsBesprechen_thenReturnsProjectTimeEntry() {
        ProjektzeitType projektzeitType = pojektzeitTypeFor(Task.BESPRECHEN.name());

        ProjectEntry actual = mapper.map(projektzeitType);

        assertTrue(actual instanceof ProjectTimeEntry);
    }

    @Test
    void withTaskIsDokumentieren_thenReturnsProjectTimeEntry() {
        ProjektzeitType projektzeitType = pojektzeitTypeFor(Task.DOKUMENTIEREN.name());

        ProjectEntry actual = mapper.map(projektzeitType);

        assertTrue(actual instanceof ProjectTimeEntry);
    }

    @Test
    void withFullSettings_thenReturnsProjectTimeEntry() {
        ProjektzeitType projektzeitType = new ProjektzeitType();
        projektzeitType.setDatum("2020-07-01");
        projektzeitType.setVon("08:00");
        projektzeitType.setBis("10:00");
        projektzeitType.setTaetigkeit(Task.BEARBEITEN.name());
        projektzeitType.setOrt(WorkingLocation.MAIN.zepOrt);
        final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm");

        ProjectTimeEntry actual = (ProjectTimeEntry) mapper.map(projektzeitType);

        assertEquals(projektzeitType.getDatum(), actual.getDate().format(dateFormatter));
        assertEquals(projektzeitType.getVon(), actual.getFromTime().format(timeFormatter));
        assertEquals(projektzeitType.getBis(), actual.getToTime().format(timeFormatter));
        assertEquals(projektzeitType.getTaetigkeit().toUpperCase(), actual.getTask().name());
    }

    @Test
    void withTaskIsReisen_thenReturnsJourneyTimeEntry() {
        ProjektzeitType projektzeitType = projektzeitTypeFor(Task.REISEN.name(), WorkingLocation.MAIN.zepOrt, JourneyDirection.TO.getDirection(),
                Vehicle.CAR_ACTIVE.id);

        ProjectEntry actual = mapper.map(projektzeitType);

        assertTrue(actual instanceof JourneyTimeEntry);
    }

    @Test
    void withFullSettings_thenReturnsJourneyTimeEntry() {
        ProjektzeitType projektzeitType = new ProjektzeitType();
        projektzeitType.setDatum("2020-07-01");
        projektzeitType.setVon("08:00");
        projektzeitType.setBis("10:00");
        projektzeitType.setTaetigkeit(Task.REISEN.name());
        projektzeitType.setReiseRichtung(JourneyDirection.TO.getDirection());
        projektzeitType.setOrt(WorkingLocation.MAIN.zepOrt);
        final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm");

        JourneyTimeEntry actual = (JourneyTimeEntry) mapper.map(projektzeitType);

        assertEquals(projektzeitType.getDatum(), actual.getDate().format(dateFormatter));
        assertEquals(projektzeitType.getVon(), actual.getFromTime().format(timeFormatter));
        assertEquals(projektzeitType.getBis(), actual.getToTime().format(timeFormatter));
        assertEquals(projektzeitType.getTaetigkeit().toUpperCase(), actual.getTask().name());
        assertEquals(projektzeitType.getOrt(), WorkingLocation.MAIN.zepOrt);
        assertEquals(projektzeitType.getReiseRichtung(), JourneyDirection.TO.getDirection());
    }

    @Test
    void whenEmptyList_thenReturnsEmptyList() {
        assertTrue(mapper.mapList(List.of()).isEmpty());
    }

    @Test
    void whenNullInList_thenFiltersNullElement() {
        assertTrue(mapper.mapList(Collections.singletonList(null)).isEmpty());
    }

    @Test
    void whenProjectTimeEntry_thenReturnsProjectTimeEntry() {
        ProjektzeitType projektzeitType = pojektzeitTypeFor(Task.BEARBEITEN.name());

        List<ProjectEntry> actual = mapper.mapList(List.of(projektzeitType));

        assertEquals(1, actual.size());
        assertTrue(actual.get(0) instanceof ProjectTimeEntry);
    }

    @Test
    void whenJourneyTimeEntry_thenReturnsJourneyTimeEntry() {
        ProjektzeitType projektzeitType = projektzeitTypeFor(Task.REISEN.name(), WorkingLocation.MAIN.zepOrt, JourneyDirection.TO.getDirection(),
                Vehicle.CAR_ACTIVE.id);

        List<ProjectEntry> actual = mapper.mapList(List.of(projektzeitType));

        assertEquals(1, actual.size());
        assertTrue(actual.get(0) instanceof JourneyTimeEntry);
    }
}