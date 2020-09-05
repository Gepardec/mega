package com.gepardec.mega.domain.model.monthlyreport;

public enum Warning {
    //time warnings
    EXCESS_WORKTIME,
    MISSING_BREAKTIME,
    TIME_TOO_EARLY_START,
    TIME_TOO_LATE_END,
    MISSING_RESTTIME,
    //journey warnings
    JOURNEY_BACK_MISSING,
    JOURNEY_TO_MISSING,
    JOURNEY_TO_AND_BACK_MISSING,
    JOURNEY_INVALID
}
