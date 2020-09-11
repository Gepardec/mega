package com.gepardec.mega.zep.mapper;

import com.gepardec.mega.domain.model.monthlyreport.JourneyTimeEntry;
import com.gepardec.mega.domain.model.monthlyreport.ProjectEntry;
import com.gepardec.mega.domain.model.monthlyreport.ProjectTimeEntry;
import de.provantis.zep.ProjektzeitType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProjectTimeMapperTest {

    private ProjectTimeMapper mapper;

    @BeforeEach
    void beforeEach() {
        mapper = new ProjectTimeMapper();
    }

    @Test
    void mapSingleTypeToEntry_twhenTaetigkeitNotEnumType_thenThrowsException() {
        ProjektzeitType projektzeitType = createDefaultProjektzeitType();
        projektzeitType.setTaetigkeit("testen");

        assertThrows(IllegalArgumentException.class, () -> mapper.mapToEntryList(List.of(projektzeitType)));
    }

    @Test
    void mapSingleTypeToEntry_whenTaetigkeitEqualsReiseAndReiseRichtungNotEnumType_thenThrowsException() {
        ProjektzeitType projektzeitType = createDefaultProjektzeitType();
        projektzeitType.setTaetigkeit("reisen");
        projektzeitType.setReiseRichtung("100");

        assertThrows(IllegalArgumentException.class, () -> mapper.mapToEntryList(List.of(projektzeitType)));
    }

    @Test
    void mapSingleTypeToEntry_whenTaetigkeitEqualsReiseAndReiseRichtungIsTO_thenReturnsProjectTimeEntryInstance() {
        ProjektzeitType projektzeitType = createDefaultProjektzeitType();

        List<ProjectEntry> result = mapper.mapToEntryList(List.of(projektzeitType));

        assertEquals(1, result.size());
        assertTrue(result.get(0) instanceof ProjectTimeEntry);
    }

    @Test
    void mapSingleTypeToEntry_whenTaetigkeitEqualsReiseAndReiseRichtungIsTO_thenReturnsMappedProjectTimeEntry() {
        ProjektzeitType projektzeitType = createDefaultProjektzeitType();
        final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm");

        List<ProjectEntry> result = mapper.mapToEntryList(List.of(projektzeitType));

        assertEquals(1, result.size());
        final ProjectTimeEntry projectTimeEntry = (ProjectTimeEntry) result.get(0);
        assertEquals(projektzeitType.getDatum(), projectTimeEntry.getDate().format(dateFormatter));
        assertEquals(projektzeitType.getVon(), projectTimeEntry.getFromTime().format(timeFormatter));
        assertEquals(projektzeitType.getBis(), projectTimeEntry.getToTime().format(timeFormatter));
        assertEquals(projektzeitType.getTaetigkeit().toUpperCase(), projectTimeEntry.getTask().name());
    }

    @Test
    void mapSingleTypeToEntry_whenTaetigkeitEqualsReiseAndReiseRichtungIsTo_thenReturnsJourneyTimeEntryInstance() {
        ProjektzeitType projektzeitType = createDefaultProjektzeitType();
        projektzeitType.setTaetigkeit("reisen");
        projektzeitType.setReiseRichtung("0");  // JourneyDirection.TO

        List<ProjectEntry> result = mapper.mapToEntryList(List.of(projektzeitType));

        assertEquals(1, result.size());
        assertTrue(result.get(0) instanceof JourneyTimeEntry);
    }

    @Test
    void mapSingleTypeToEntry_whenTaetigkeitIsReiseAndReiseRichtungIsTO_thenReturnsMappedJourneyTimeEntry() {
        ProjektzeitType projektzeitType = createDefaultProjektzeitType();
        projektzeitType.setTaetigkeit("reisen");
        projektzeitType.setReiseRichtung("0");  // JourneyDirection.TO
        final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm");

        List<ProjectEntry> result = mapper.mapToEntryList(List.of(projektzeitType));

        assertEquals(1, result.size());
        final JourneyTimeEntry journeyTimeEntry = (JourneyTimeEntry) result.get(0);
        assertEquals(projektzeitType.getReiseRichtung(), journeyTimeEntry.getJourneyDirection().getDirection());
        assertEquals(projektzeitType.getDatum(), journeyTimeEntry.getDate().format(dateFormatter));
        assertEquals(projektzeitType.getVon(), journeyTimeEntry.getFromTime().format(timeFormatter));
        assertEquals(projektzeitType.getBis(), journeyTimeEntry.getToTime().format(timeFormatter));
        assertEquals(projektzeitType.getTaetigkeit().toUpperCase(), journeyTimeEntry.getTask().name());
    }

    private static ProjektzeitType createDefaultProjektzeitType() {
        ProjektzeitType projektzeitType = new ProjektzeitType();
        projektzeitType.setDatum("2020-07-01");
        projektzeitType.setVon("08:00");
        projektzeitType.setBis("10:00");
        projektzeitType.setTaetigkeit("bearbeiten");
        return projektzeitType;
    }
}
