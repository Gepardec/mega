package com.gepardec.mega.domain.model.monthlyreport;

public enum TimeWarningType implements WarningType {
    OUTSIDE_CORE_WORKING_TIME,
    TIME_OVERLAP;

    @Override
    public String warningType() {
        return "time";
    }
}
