package com.gepardec.mega.service.impl.monthlyreport.calculation.journey;

import com.gepardec.mega.domain.model.monthlyreport.JourneyDirection;
import com.gepardec.mega.domain.model.monthlyreport.Warning;

public class JourneyDirectionValidator {

    private JourneyDirection formerDirection = null;

    public Warning validate(JourneyDirection currentDirection, JourneyDirection nextDirection) {
        Warning warning = null;
        switch (currentDirection) {
            case TO:
                if (formerDirection == JourneyDirection.TO || formerDirection == JourneyDirection.FURTHER) {
                    warning = Warning.JOURNEY_BACK_MISSING;
                }
                formerDirection = currentDirection;
                break;
            case FURTHER:
            case BACK:
                if (formerDirection == null || formerDirection == JourneyDirection.BACK) {
                    warning = Warning.JOURNEY_TO_MISSING;
                }
                if (formerDirection != null) {
                    formerDirection = currentDirection;
                }
                break;
            default:
                throw new IllegalArgumentException("Enum type '" + currentDirection.name() + "' not supported!");
        }
        // Handles the last entry, or if the next one is starting a journey while a journey is still open
        if (warning == null && !isFinished()) {
            if (nextDirection == null || isStartingNewJourney(nextDirection)) {
                formerDirection = null;
                return Warning.JOURNEY_BACK_MISSING;
            }
        }
        return warning;
    }

    public boolean isFinished() {
        return formerDirection != null && formerDirection == JourneyDirection.BACK;
    }

    private boolean isStartingNewJourney(final JourneyDirection direction) {
        return JourneyDirection.TO.equals(direction);
    }
}
