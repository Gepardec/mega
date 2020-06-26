package com.gepardec.mega.communication;


public enum Reminder {
    EMPLOYEE_CHECK_PROJECTTIME(1, Remindertype.WORKING_DAY_BASED),
    OM_CONTROL_EMPLOYEES_CONTENT(3, Remindertype.WORKING_DAY_BASED),
    PL_PROJECT_CONTROLLING(5, Remindertype.WORKING_DAY_BASED),
    OM_RELEASE(8, Remindertype.WORKING_DAY_BASED),
    OM_ADMINISTRATIVE(15, Remindertype.DAY_OF_MONTH_BASED),
    OM_SALARY(-3, Remindertype.WORKING_DAY_BASED),
    OM_CONTROL_PROJECTTIMES(-1, Remindertype.WORKING_DAY_BASED);

    private int day;
    private Remindertype type;

    Reminder(int day, Remindertype type) {
        this.day = day;
        this.type = type;
    }

    public int getDay() {
        return day;
    }

    public Remindertype getType() {
        return type;
    }


}
