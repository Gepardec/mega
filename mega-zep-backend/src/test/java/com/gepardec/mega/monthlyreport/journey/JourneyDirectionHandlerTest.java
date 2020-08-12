package com.gepardec.mega.monthlyreport.journey;


import com.gepardec.mega.domain.model.monthlyreport.Warning;
import com.gepardec.mega.service.impl.monthlyreport.JourneyDirectionHandler;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.gepardec.mega.domain.model.monthlyreport.JourneyDirection.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class JourneyDirectionHandlerTest {

    private JourneyDirectionHandler journeyDirectionHandler;

    @Test
    void moveTo_foAimAndBack_noWarning() {
        journeyDirectionHandler = new JourneyDirectionHandler();
        assertAll(
                () -> assertEmpty(journeyDirectionHandler.moveTo(TO_AIM)),
                () -> assertEmpty(journeyDirectionHandler.moveTo(FURTHER)),
                () -> assertEmpty(journeyDirectionHandler.moveTo(FURTHER)),
                () -> assertEmpty(journeyDirectionHandler.moveTo(BACK)));
    }

    @Test
    void moveTo_Further_shouldReturnToAimMissingError() {
        journeyDirectionHandler = new JourneyDirectionHandler();
        assertToAimMissing(journeyDirectionHandler.moveTo(FURTHER));
    }

    @Test
    void moveTo_Back_shouldReturnToAimMissingError() {
        journeyDirectionHandler = new JourneyDirectionHandler();
        assertToAimMissing(journeyDirectionHandler.moveTo(BACK));
    }

    @Test
    void moveTo_ToAimToAim_shouldReturnBackMissingError() {
        journeyDirectionHandler = new JourneyDirectionHandler();
        assertAll(
                () -> assertEmpty(journeyDirectionHandler.moveTo(TO_AIM)),
                () -> assertBackMissing(journeyDirectionHandler.moveTo(TO_AIM)));
    }

    @Test
    void moveTo_twoErrorWithACorrectInTheMiddle_shouldReturnErrors() {
        journeyDirectionHandler = new JourneyDirectionHandler();
        assertAll(
                () -> assertToAimMissing(journeyDirectionHandler.moveTo(BACK)),
                () -> assertEmpty(journeyDirectionHandler.moveTo(TO_AIM)),
                () -> assertEmpty(journeyDirectionHandler.moveTo(BACK)),
                () -> assertToAimMissing(journeyDirectionHandler.moveTo(FURTHER)));
    }

    @Test
    void moveTo_ToAimFurtherToAim_shouldReturnToBackMissingError() {
        journeyDirectionHandler = new JourneyDirectionHandler();
        assertAll(
                () -> assertEmpty(journeyDirectionHandler.moveTo(TO_AIM)),
                () -> assertEmpty(journeyDirectionHandler.moveTo(FURTHER)),
                () -> assertBackMissing(journeyDirectionHandler.moveTo(TO_AIM))
        );
    }

    private static void assertEmpty(Optional<Warning> warning) {
        assertEquals(Optional.empty(), warning);
    }

    private static void assertToAimMissing(Optional<Warning> warning) {
        assertEquals(Optional.of(Warning.WARNING_JOURNEY_TO_AIM_MISSING), warning);
    }

    private static void assertBackMissing(Optional<Warning> warning) {
        assertEquals(Optional.of(Warning.WARNING_JOURNEY_BACK_MISSING), warning);
    }

}
