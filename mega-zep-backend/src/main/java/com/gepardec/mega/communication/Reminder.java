package com.gepardec.mega.communication;

public enum Reminder {
    EMPLOYEE_CHECK_PROJECTTIME(1),
    OM_CHECK_EMPLOYEES_CONTENT(3),
    PL_PROJECT_CONTROLLING(5),
    OM_RELEASE(8),
    OM_SALARY_CHARGING(15),
    OM_SALARY_TRANSFER(-3);

    private int workingDay;

    Reminder(int workingDay) {
        this.workingDay = workingDay;
    }

    public int getWorkingDay() {
        return workingDay;
    }
}
