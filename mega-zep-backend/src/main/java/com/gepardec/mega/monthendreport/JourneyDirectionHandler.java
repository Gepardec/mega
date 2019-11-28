package com.gepardec.mega.monthendreport;

import java.util.Optional;

import static com.gepardec.mega.monthendreport.JourneyDirection.*;

public class JourneyDirectionHandler {

    private JourneyDirection beforeJourneyDirection = BACK;

    private void resetHandler() {
        beforeJourneyDirection = BACK;
    }

    public Optional<WarningType> moveTo(JourneyDirection journeyDirection) {

        if (journeyDirection == TO_AIM) {
            if (beforeJourneyDirection == TO_AIM || beforeJourneyDirection == FURTHER) {
                resetHandler();
                return Optional.of(WarningType.WARNING_JOURNEY_BACK_MISSING);
            }
        } else if ((journeyDirection == FURTHER || journeyDirection == BACK)
                && beforeJourneyDirection == BACK) {
            resetHandler();
            return Optional.of(WarningType.WARNING_JOURNEY_TO_AIM_MISSING);
        }
        beforeJourneyDirection = journeyDirection;
        return Optional.empty();
    }


}
