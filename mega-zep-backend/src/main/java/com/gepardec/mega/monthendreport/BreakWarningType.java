package com.gepardec.mega.monthendreport;

public enum BreakWarningType {
    WARNING_MORE_THAN_10_HOURS("Warnung: Sie haben mehr als 10 Stunden eingetragen"),
    WARNING_NO_BREAK("Warnung: Sie haben an diesem Tag keine Pause eingetragen");

    private String text;

    BreakWarningType(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
