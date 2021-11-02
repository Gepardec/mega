package com.gepardec.mega.domain.model.monthlyreport;

public enum JourneyWarningType implements WarningType {
    BACK_MISSING,
    TO_MISSING,
    INVALID_WORKING_LOCATION;

    @Override
    public String warningType() {
        return "journey";
    }
}
