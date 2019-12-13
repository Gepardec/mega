package com.gepardec.mega.monthlyreport;

import java.util.Optional;

import static com.gepardec.mega.monthlyreport.JourneyDirection.*;

public class JourneyDirectionHandler {

    private JourneyDirection beforeJourneyDirection = BACK;

    private void resetHandler() {
        beforeJourneyDirection = BACK;
    }

    public Optional<Warning> moveTo(JourneyDirection journeyDirection) {

        if (journeyDirection == TO_AIM) {
            if (beforeJourneyDirection == TO_AIM || beforeJourneyDirection == FURTHER) {
                resetHandler();
                return Optional.of(Warning.WARNING_JOURNEY_BACK_MISSING);
            }
        } else if ((journeyDirection == FURTHER || journeyDirection == BACK)
                && beforeJourneyDirection == BACK) {
            resetHandler();
            return Optional.of(Warning.WARNING_JOURNEY_TO_AIM_MISSING);
        }
        beforeJourneyDirection = journeyDirection;
        return Optional.empty();
    }


}
