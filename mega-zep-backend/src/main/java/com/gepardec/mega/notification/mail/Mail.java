package com.gepardec.mega.notification.mail;

public enum Mail {
    EMPLOYEE_CHECK_PROJECTTIME(1, MailType.WORKING_DAY_BASED, "emails/reminder-template.html"),
    OM_CONTROL_EMPLOYEES_CONTENT(3, MailType.WORKING_DAY_BASED, "emails/reminder-template.html"),
    PL_PROJECT_CONTROLLING(5, MailType.WORKING_DAY_BASED, "emails/reminder-template.html"),
    OM_RELEASE(8, MailType.WORKING_DAY_BASED, "emails/reminder-template.html"),
    OM_ADMINISTRATIVE(15, MailType.DAY_OF_MONTH_BASED, "emails/reminder-template.html"),
    OM_SALARY(-3, MailType.WORKING_DAY_BASED, "emails/reminder-template.html"),
    OM_CONTROL_PROJECTTIMES(-1, MailType.WORKING_DAY_BASED, "emails/reminder-template.html"),
    COMMENT_CLOSED(MailType.MANUAL),
    COMMENT_CREATED(MailType.MANUAL),
    COMMENT_DELETED(MailType.MANUAL),
    COMMENT_MODIFIED(MailType.MANUAL);

    private final Integer day;

    private final MailType type;

    private final String template;

    Mail(MailType type) {
        this(null, type, null);
    }

    Mail(Integer day, MailType type, String template) {
        this.day = day;
        this.type = type;
        this.template = template;
    }

    public Integer getDay() {
        return day;
    }

    public MailType getType() {
        return type;
    }

    public String getTemplate() {
        return template;
    }
}
