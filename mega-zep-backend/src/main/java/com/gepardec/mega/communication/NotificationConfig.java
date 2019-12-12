package com.gepardec.mega.communication;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class NotificationConfig {

    @ConfigProperty(name = "mega.mail.reminder.employee.path")
    String employeePath;

    @ConfigProperty(name = "mega.mail.reminder.employee.subject")
    String employeeSubject;

    @ConfigProperty(name = "mega.mail.reminder.pl.path")
    String plPath;

    @ConfigProperty(name = "mega.mail.reminder.pl.subject")
    String plSubject;

    @ConfigProperty(name = "mega.mail.reminder.om.controlEmployeesData.path")
    String omControlEmployeesDataPath;

    @ConfigProperty(name = "mega.mail.reminder.om.controlEmployeesData.subject")
    String omControlEmployeesDataSubject;

    @ConfigProperty(name = "mega.mail.reminder.om.release.path")
    String omReleasePath;

    @ConfigProperty(name = "mega.mail.reminder.om.release.subject")
    String omReleaseSubject;

    @ConfigProperty(name = "mega.mail.reminder.om.administrative.path")
    String omAdministrativePath;

    @ConfigProperty(name = "mega.mail.reminder.om.administrative.subject")
    String omAdministrativeSubject;

    @ConfigProperty(name = "mega.mail.reminder.om.salary.path")
    String omSalaryPath;

    @ConfigProperty(name = "mega.mail.reminder.om.salary.subject")
    String omSalarySubject;


    public String getPathByReminder(Reminder reminder) {
        String path = null;
        switch (reminder) {
            case EMPLOYEE_CHECK_PROJECTTIME: {
                path = employeePath;
                break;
            }
            case PL_PROJECT_CONTROLLING: {
                path = plPath;
                break;
            }
            case OM_CONTROL_EMPLOYEES_CONTENT: {
                path = omControlEmployeesDataPath;
                break;
            }
            case OM_RELEASE: {
                path = omReleasePath;
                break;
            }
            case OM_ADMINISTRATIVE: {
                path = omAdministrativePath;
                break;
            }
            case OM_SALARY: {
                path = omSalaryPath;
                break;
            }
            default: {
                break;
            }
        }
        return path;
    }

    public String getSubjectByReminder(Reminder reminder) {
        String subject = null;
        switch (reminder) {
            case EMPLOYEE_CHECK_PROJECTTIME: {
                subject = employeePath;
                break;
            }
            case PL_PROJECT_CONTROLLING: {
                subject = plPath;
                break;
            }
            case OM_CONTROL_EMPLOYEES_CONTENT: {
                subject = omControlEmployeesDataSubject;
                break;
            }
            case OM_RELEASE: {
                subject = omReleaseSubject;
                break;
            }
            case OM_ADMINISTRATIVE: {
                subject = omAdministrativeSubject;
                break;
            }
            case OM_SALARY: {
                subject = omSalarySubject;
                break;
            }
            default: {
                break;
            }
        }
        return subject;
    }
}
