package com.gepardec.mega.domain.calculation.journey;

import com.gepardec.mega.domain.model.monthlyreport.JourneyDirection;
import com.gepardec.mega.domain.model.monthlyreport.JourneyWarningType;

public class JourneyDirectionValidator {

    private JourneyDirection formerDirection = null;

    public JourneyWarningType validate(JourneyDirection currentDirection, JourneyDirection nextDirection) {
        JourneyWarningType warning = null;
        switch (currentDirection) {
            case TO:
                if (formerDirection == JourneyDirection.TO || formerDirection == JourneyDirection.FURTHER) {
                    warning = JourneyWarningType.BACK_MISSING;
                }
                formerDirection = currentDirection;
                break;
            case FURTHER:
            case BACK:
                if (formerDirection == null || formerDirection == JourneyDirection.BACK) {
                    warning = JourneyWarningType.TO_MISSING;
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
                return JourneyWarningType.BACK_MISSING;
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
