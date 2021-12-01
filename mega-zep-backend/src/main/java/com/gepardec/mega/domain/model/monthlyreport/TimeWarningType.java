package com.gepardec.mega.domain.model.monthlyreport;

public enum TimeWarningType implements WarningType {
    OUTSIDE_CORE_WORKING_TIME,
    NO_TIME_ENTRY,
    EMPTY_ENTRY_LIST;

    @Override
    public String warningType() {
        return "time";
    }
}
