package com.gepardec.mega.monthendreport;


import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.gepardec.mega.monthendreport.JourneyDirection.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class JourneyDirectionHandlerTest {

    @Test
    void moveTo_foAimAndBack_noWarning() {
        JourneyDirectionHandler journeyDirectionHandler = new JourneyDirectionHandler();
        assertEquals(Optional.empty(), journeyDirectionHandler.moveTo(TO_AIM));
        assertEquals(Optional.empty(), journeyDirectionHandler.moveTo(FURTHER));
        assertEquals(Optional.empty(), journeyDirectionHandler.moveTo(FURTHER));
        assertEquals(Optional.empty(), journeyDirectionHandler.moveTo(BACK));
    }

    @Test
    void moveTo_further_warningNoReturn() {
        JourneyDirectionHandler journeyDirectionHandler = new JourneyDirectionHandler();
        assertEquals(Optional.of(WarningType.WARNING_JOURNEY_TO_AIM_MISSING), journeyDirectionHandler.moveTo(FURTHER));
    }

}
