package com.gepardec.mega.communication;

public enum Reminder {
    EMPLOYEE_CHECK_PROJECTTIME(1),
    OM_CONTROL_EMPLOYEES_CONTENT(3),
    PL_PROJECT_CONTROLLING(5),
    OM_RELEASE(8),
    OM_ADMINISTRATIVE(15),
    OM_SALARY(-3);

    private int workingDay;

    Reminder(int workingDay) {
        this.workingDay = workingDay;
    }

    public int getWorkingDay() {
        return workingDay;
    }
}
