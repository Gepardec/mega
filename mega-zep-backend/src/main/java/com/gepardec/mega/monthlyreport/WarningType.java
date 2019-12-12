package com.gepardec.mega.monthlyreport;

public enum WarningType {
    WARNING_TIME_MORE_THAN_10_HOURS("Warnung: Sie haben mehr als 10 Stunden eingetragen"),
    WARNING_TIME_TOO_LESS_BREAK("Warnung: Sie haben zu wenig Pause eingetragen"),
    WARNING_TIME_TOO_EARLY_START("Warnung: Sie haben zu frühe Arbeitszeit eingetragen"),
    WARNING_TIME_TOO_LATE_END("Warnung: Sie haben zu späte Arbeitszeit eingetragen"),
    WARNING_TIME_TOO_LESS_REST("Warnung: Sie haben zu wenig Ruhezeit eingetragen"),


    WARNING_JOURNEY_BACK_MISSING("Warnung: Rückreise fehlt oder ist nach dem Zeitraum"),
    WARNING_JOURNEY_TO_AIM_MISSING("Warnung: Hinreise fehlt oder ist vor dem Zeitraum");


    private String text;

    WarningType(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
