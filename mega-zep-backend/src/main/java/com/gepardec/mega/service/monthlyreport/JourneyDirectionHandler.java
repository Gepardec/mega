package com.gepardec.mega.service.monthlyreport;

import com.gepardec.mega.domain.model.JourneyDirection;
import com.gepardec.mega.domain.model.Warning;

import java.util.Optional;

public class JourneyDirectionHandler {

    private JourneyDirection beforeJourneyDirection = JourneyDirection.BACK;

    private void resetHandler() {
        beforeJourneyDirection = JourneyDirection.BACK;
    }

    public Optional<Warning> moveTo(JourneyDirection journeyDirection) {

        if (journeyDirection == JourneyDirection.TO_AIM) {
            if (beforeJourneyDirection == JourneyDirection.TO_AIM || beforeJourneyDirection == JourneyDirection.FURTHER) {
                resetHandler();
                return Optional.of(Warning.WARNING_JOURNEY_BACK_MISSING);
            }
        } else if ((journeyDirection == JourneyDirection.FURTHER || journeyDirection == JourneyDirection.BACK)
                && beforeJourneyDirection == JourneyDirection.BACK) {
            resetHandler();
            return Optional.of(Warning.WARNING_JOURNEY_TO_AIM_MISSING);
        }
        beforeJourneyDirection = journeyDirection;
        return Optional.empty();
    }


}
