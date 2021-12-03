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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
        assertThat(mapper.map(null)).isNull();
    }

    @Test
    void withTaetigkeitIsNotATask_thenThrowsIllegalArgumentException() {
        ProjektzeitType projektzeitType = projektzeitTypeFor("testen", WorkingLocation.MAIN.zepOrt, JourneyDirection.TO.getDirection(),
                Vehicle.OTHER_INACTIVE.id);

        assertThatThrownBy(() -> mapper.map(projektzeitType)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void withOrtIsNotAWorkingLocation_thenThrowsIllegalArgumentException() {
        ProjektzeitType projektzeitType = projektzeitTypeFor(Task.REISEN.name(), "unbekannt", JourneyDirection.TO.getDirection(), Vehicle.OTHER_INACTIVE.id);

        assertThatThrownBy(() -> mapper.map(projektzeitType)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void withReiseRichtungIsNotAJourneyDirection_thenThrowsIllegalArgumentException() {
        ProjektzeitType projektzeitType = projektzeitTypeFor(Task.REISEN.name(), WorkingLocation.MAIN.zepOrt, "100", Vehicle.OTHER_INACTIVE.id);

        assertThatThrownBy(() -> mapper.map(projektzeitType)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void withFahrzeugIsNotAVehicle_thenThrowsIllegalArgumentException() {
        ProjektzeitType projektzeitType = projektzeitTypeFor(Task.REISEN.name(), WorkingLocation.MAIN.zepOrt, JourneyDirection.TO.getDirection(),
                "UNKOWN");

        assertThatThrownBy(() -> mapper.map(projektzeitType)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void withTaskIsBearbeiten_thenReturnsProjectTimeEntry() {
        ProjektzeitType projektzeitType = pojektzeitTypeFor(Task.BEARBEITEN.name());

        ProjectEntry actual = mapper.map(projektzeitType);

        assertThat(actual).isInstanceOf(ProjectTimeEntry.class);
    }

    @Test
    void withTaskIsBesprechen_thenReturnsProjectTimeEntry() {
        ProjektzeitType projektzeitType = pojektzeitTypeFor(Task.BESPRECHEN.name());

        ProjectEntry actual = mapper.map(projektzeitType);

        assertThat(actual).isInstanceOf(ProjectTimeEntry.class);
    }

    @Test
    void withTaskIsDokumentieren_thenReturnsProjectTimeEntry() {
        ProjektzeitType projektzeitType = pojektzeitTypeFor(Task.DOKUMENTIEREN.name());

        ProjectEntry actual = mapper.map(projektzeitType);

        assertThat(actual).isInstanceOf(ProjectTimeEntry.class);
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

        assertThat(actual.getDate().format(dateFormatter)).isEqualTo(projektzeitType.getDatum());
        assertThat(actual.getFromTime().format(timeFormatter)).isEqualTo(projektzeitType.getVon());
        assertThat(actual.getToTime().format(timeFormatter)).isEqualTo(projektzeitType.getBis());
        assertThat(actual.getTask().name()).isEqualTo(projektzeitType.getTaetigkeit().toUpperCase());
    }

    @Test
    void withTaskIsReisen_thenReturnsJourneyTimeEntry() {
        ProjektzeitType projektzeitType = projektzeitTypeFor(Task.REISEN.name(), WorkingLocation.MAIN.zepOrt, JourneyDirection.TO.getDirection(),
                Vehicle.CAR_ACTIVE.id);

        ProjectEntry actual = mapper.map(projektzeitType);

        assertThat(actual).isInstanceOf(JourneyTimeEntry.class);
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

        assertThat(actual.getDate().format(dateFormatter)).isEqualTo(projektzeitType.getDatum());
        assertThat(actual.getFromTime().format(timeFormatter)).isEqualTo(projektzeitType.getVon());
        assertThat(actual.getToTime().format(timeFormatter)).isEqualTo(projektzeitType.getBis());
        assertThat(actual.getTask().name()).isEqualTo(projektzeitType.getTaetigkeit().toUpperCase());
        assertThat(WorkingLocation.MAIN.zepOrt).isEqualTo(projektzeitType.getOrt());
        assertThat(JourneyDirection.TO.getDirection()).isEqualTo(projektzeitType.getReiseRichtung());
    }

    @Test
    void whenEmptyList_thenReturnsEmptyList() {
        assertThat(mapper.mapList(List.of())).isEmpty();
    }

    @Test
    void whenNullInList_thenFiltersNullElement() {
        assertThat(mapper.mapList(Collections.singletonList(null))).isEmpty();
    }

    @Test
    void whenProjectTimeEntry_thenReturnsProjectTimeEntry() {
        ProjektzeitType projektzeitType = pojektzeitTypeFor(Task.BEARBEITEN.name());

        List<ProjectEntry> actual = mapper.mapList(List.of(projektzeitType));

        assertThat(actual).hasSize(1);
        assertThat(actual.get(0)).isInstanceOf(ProjectTimeEntry.class);
    }

    @Test
    void whenJourneyTimeEntry_thenReturnsJourneyTimeEntry() {
        ProjektzeitType projektzeitType = projektzeitTypeFor(Task.REISEN.name(), WorkingLocation.MAIN.zepOrt, JourneyDirection.TO.getDirection(),
                Vehicle.CAR_ACTIVE.id);

        List<ProjectEntry> actual = mapper.mapList(List.of(projektzeitType));

        assertThat(actual).hasSize(1);
        assertThat(actual.get(0)).isInstanceOf(JourneyTimeEntry.class);
    }
}
