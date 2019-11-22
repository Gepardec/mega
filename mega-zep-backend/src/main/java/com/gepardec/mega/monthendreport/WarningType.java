package com.gepardec.mega.monthendreport;

public enum WarningType {
    WARNING_MORE_THAN_10_HOURS("Warnung: Sie haben mehr als 10 Stunden eingetragen"),
    WARNING_TOO_LESS_BREAK("Warnung: Sie haben zu wenig Pause eingetragen"),
    WARNING_TOO_LESS_REST("Warnung: Sie haben zu wenig Ruhezeit eingetragen");

    private String text;

    WarningType(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
