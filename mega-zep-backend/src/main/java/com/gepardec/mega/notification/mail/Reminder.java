package com.gepardec.mega.notification.mail;


public enum Reminder {
    EMPLOYEE_CHECK_PROJECTTIME(1, ReminderType.WORKING_DAY_BASED),
    OM_CONTROL_EMPLOYEES_CONTENT(3, ReminderType.WORKING_DAY_BASED),
    PL_PROJECT_CONTROLLING(5, ReminderType.WORKING_DAY_BASED),
    OM_RELEASE(8, ReminderType.WORKING_DAY_BASED),
    OM_ADMINISTRATIVE(15, ReminderType.DAY_OF_MONTH_BASED),
    OM_SALARY(-3, ReminderType.WORKING_DAY_BASED),
    OM_CONTROL_PROJECTTIMES(-1, ReminderType.WORKING_DAY_BASED);

    private Integer day;
    private ReminderType type;

    Reminder(Integer day, ReminderType type) {
        this.day = day;
        this.type = type;
    }

    public Integer getDay() {
        return day;
    }

    public ReminderType getType() {
        return type;
    }


}
