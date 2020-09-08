package com.gepardec.mega.zep.mapper;

import com.gepardec.mega.domain.model.monthlyreport.JourneyTimeEntry;
import com.gepardec.mega.domain.model.monthlyreport.ProjectEntry;
import com.gepardec.mega.domain.model.monthlyreport.ProjectTimeEntry;
import de.provantis.zep.ProjektzeitType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProjectTimeMapperTest {

    @Test
    void mapSingleTypeToEntry_taetigkeitNotEnumType_ThrowsException() {
        ProjektzeitType projektzeitType = createDefaultProjektzeitType();
        projektzeitType.setTaetigkeit("testen");

        assertThrows(IllegalArgumentException.class, () -> ProjectTimeMapper.mapToEntryList(List.of(projektzeitType)));
    }

    @Test
    void mapSingleTypeToEntry_taetigkeitEqualsReiseAndReiseRichtungNotEnumType_ThrowsException() {
        ProjektzeitType projektzeitType = createDefaultProjektzeitType();
        projektzeitType.setTaetigkeit("reisen");
        projektzeitType.setReiseRichtung("100");

        assertThrows(IllegalArgumentException.class, () -> ProjectTimeMapper.mapToEntryList(List.of(projektzeitType)));
    }

    @Test
    void mapSingleTypeToEntry_taetigkeitEqualsReiseAndReiseRichtungEqualsTo_ReturnsProjectTimeEntry() {
        ProjektzeitType projektzeitType = createDefaultProjektzeitType();

        List<ProjectEntry> result = ProjectTimeMapper.mapToEntryList(List.of(projektzeitType));

        assertEquals(1, result.size());
        assertTrue(result.get(0) instanceof ProjectTimeEntry);
    }

    @Test
    void mapSingleTypeToEntry_taetigkeitEqualsReiseAndReiseRichtungEqualsTo_ReturnsJourneyTimeEntry() {
        ProjektzeitType projektzeitType = createDefaultProjektzeitType();
        projektzeitType.setTaetigkeit("reisen");
        projektzeitType.setReiseRichtung("0");  // JourneyDirection.TO

        List<ProjectEntry> result = ProjectTimeMapper.mapToEntryList(List.of(projektzeitType));

        assertEquals(1, result.size());
        assertTrue(result.get(0) instanceof JourneyTimeEntry);
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
