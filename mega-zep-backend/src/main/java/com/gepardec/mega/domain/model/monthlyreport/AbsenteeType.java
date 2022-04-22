package com.gepardec.mega.domain.model.monthlyreport;

public enum AbsenteeType {
    COMPENSATORY_DAYS("FA"),
    VACATION_DAYS("UB"),
    SICKNESS_DAYS("KR"),
    NURSING_DAYS("PU"),
    MATERNITY_LEAVE_DAYS("KA"),
    EXTERNAL_TRAINING_DAYS("EW"),
    CONFERENCE_DAYS("KO"),
    MATERNITY_PROTECTION_DAYS("MU"),
    FATHER_MONTH_DAYS("PA"),
    PAID_SPECIAL_LEAVE_DAYS("SU"),
    NON_PAID_VACATION_DAYS("UU");

    private final String type;

    AbsenteeType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
