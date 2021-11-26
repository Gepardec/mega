package com.gepardec.mega.domain.model.monthlyreport;

public enum TimeOverlapWarningType implements WarningType {
    TIME_OVERLAP;

    @Override
    public String warningType() {
        return "time";
    }
}
