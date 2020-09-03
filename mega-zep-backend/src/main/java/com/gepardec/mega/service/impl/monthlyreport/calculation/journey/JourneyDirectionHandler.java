package com.gepardec.mega.service.impl.monthlyreport.calculation.journey;

import com.gepardec.mega.domain.model.monthlyreport.JourneyDirection;
import com.gepardec.mega.domain.model.monthlyreport.Warning;

import java.util.Optional;

public class JourneyDirectionHandler {

    private JourneyDirection beforeJourneyDirection = JourneyDirection.BACK;

    public Optional<Warning> moveTo(JourneyDirection journeyDirection) {
        Optional<Warning> warning = Optional.empty();
        switch (journeyDirection) {
            case TO_AIM:
                if (beforeJourneyDirection == JourneyDirection.TO_AIM || beforeJourneyDirection == JourneyDirection.FURTHER) {
                    warning = Optional.of(Warning.WARNING_JOURNEY_BACK_MISSING);
                }
                break;
            case FURTHER:
            case BACK:
                if (beforeJourneyDirection == JourneyDirection.BACK) {
                    beforeJourneyDirection = journeyDirection;
                    warning = Optional.of(Warning.WARNING_JOURNEY_TO_AIM_MISSING);
                }
                break;
            case INVALIDATE:
                resetHandler();
                warning = Optional.of(Warning.WARNING_JOURNEY_BACK_MISSING);
                break;
            default:
                throw new IllegalArgumentException("Enum type '" + journeyDirection.name() + "' not supported!");
        }
        beforeJourneyDirection = journeyDirection;
        return warning;
    }

    public boolean isJourneyFinished() {
        return beforeJourneyDirection == JourneyDirection.BACK;
    }

    private void resetHandler() {
        beforeJourneyDirection = JourneyDirection.BACK;
    }
}
