package com.gepardec.mega.monthlyreport;

public enum Warning {
    //time warnings
    WARNING_EXCESS_WORKTIME,
    WARNING_MISSING_BREAKTIME,
    WARNING_TIME_TOO_EARLY_START,
    WARNING_TIME_TOO_LATE_END,
    WARNING_MISSING_RESTTIME,

    //journey warnings
    WARNING_JOURNEY_BACK_MISSING,
    WARNING_JOURNEY_TO_AIM_MISSING;
}
