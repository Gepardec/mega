package com.gepardec.mega.monthlyreport.journey;


import com.gepardec.mega.domain.model.monthlyreport.Warning;
import com.gepardec.mega.service.impl.monthlyreport.JourneyDirectionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.gepardec.mega.domain.model.monthlyreport.JourneyDirection.*;
import static org.junit.jupiter.api.Assertions.*;

class JourneyDirectionHandlerTest {

    private JourneyDirectionHandler journeyDirectionHandler;

    @BeforeEach
    void beforeEach() {
        journeyDirectionHandler = new JourneyDirectionHandler();
    }

    @Test
    void moveTo_foAimAndBack_noWarning() {
        assertAll(
                () -> assertTrue(journeyDirectionHandler.moveTo(TO_AIM).isEmpty()),
                () -> assertTrue(journeyDirectionHandler.moveTo(FURTHER).isEmpty()),
                () -> assertTrue(journeyDirectionHandler.moveTo(FURTHER).isEmpty()),
                () -> assertTrue(journeyDirectionHandler.moveTo(BACK).isEmpty()));
    }

    @Test
    void moveTo_Further_shouldReturnToAimMissingError() {
        assertEquals(Optional.of(Warning.WARNING_JOURNEY_TO_AIM_MISSING), journeyDirectionHandler.moveTo(FURTHER));
    }

    @Test
    void moveTo_Back_shouldReturnToAimMissingError() {
        assertEquals(Optional.of(Warning.WARNING_JOURNEY_TO_AIM_MISSING), journeyDirectionHandler.moveTo(BACK));
    }

    @Test
    void moveTo_ToAimToAim_shouldReturnBackMissingError() {
        assertAll(
                () -> assertTrue(journeyDirectionHandler.moveTo(TO_AIM).isEmpty()),
                () -> assertEquals(Optional.of(Warning.WARNING_JOURNEY_BACK_MISSING), journeyDirectionHandler.moveTo(TO_AIM)));
    }

    @Test
    void moveTo_twoErrorWithACorrectInTheMiddle_shouldReturnErrors() {
        assertAll(
                () -> assertEquals(Optional.of(Warning.WARNING_JOURNEY_TO_AIM_MISSING), journeyDirectionHandler.moveTo(BACK)),
                () -> assertTrue(journeyDirectionHandler.moveTo(TO_AIM).isEmpty()),
                () -> assertTrue(journeyDirectionHandler.moveTo(BACK).isEmpty()),
                () -> assertEquals(Optional.of(Warning.WARNING_JOURNEY_TO_AIM_MISSING), journeyDirectionHandler.moveTo(FURTHER)));
    }

    @Test
    void moveTo_ToAimFurtherToAim_shouldReturnToBackMissingError() {
        assertAll(
                () -> assertTrue(journeyDirectionHandler.moveTo(TO_AIM).isEmpty()),
                () -> assertTrue(journeyDirectionHandler.moveTo(FURTHER).isEmpty()),
                () -> assertEquals(Optional.of(Warning.WARNING_JOURNEY_BACK_MISSING), journeyDirectionHandler.moveTo(TO_AIM)));
    }
}
