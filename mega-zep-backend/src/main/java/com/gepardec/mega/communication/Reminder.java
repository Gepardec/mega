package com.gepardec.mega.communication;

public enum Reminder {
    USER_CHECK_PROJECTTIMES(1, "Friendly Reminder: Buchungen kontrollieren"),
    OM_CHECK_USER_CONTENT(3, "Reminder: Kontrolle Userinhalte"),
    PL_CHECK_USER_CONTENT(5, "Reminder: Kontrolle Userinhalte"),
    OM_RELEASE(8, "Reminder: Freigabe durchführen"),
    OM_SALARY_CHARGING(15, "Reminder: Lohnverrechnung"),
    OM_SALARY_TRANSFER(-3, "Reminder: Löhne überweisen");

    private Integer workingDay;
    private String text;

    Reminder(Integer workingDay, String text) {
        this.workingDay = workingDay;
        this.text = text;
    }

    public Integer getWorkingDay() {
        return workingDay;
    }

    public String getText() {
        return text;
    }
}
