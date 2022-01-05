package com.gepardec.mega.domain.model.monthlyreport;

public enum AbsenteeType {
    COMPENSATORY_DAYS("FA"),
    VACATION_DAYS("UB");

    private final String type;

    AbsenteeType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
