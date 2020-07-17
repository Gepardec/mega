package com.gepardec.mega.notification.mail;

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

    @ConfigProperty(name = "mega.mail.reminder.om.controlprojecttimes.path")
    String omControlProjecttimesPath;

    @ConfigProperty(name = "mega.mail.reminder.om.controlprojecttimes.subject")
    String omControlProjecttimesSubject;


    public String getEmployeePath() {
        return employeePath;
    }

    public String getEmployeeSubject() {
        return employeeSubject;
    }

    public String getPlPath() {
        return plPath;
    }

    public String getPlSubject() {
        return plSubject;
    }

    public String getOmControlEmployeesDataPath() {
        return omControlEmployeesDataPath;
    }

    public String getOmControlEmployeesDataSubject() {
        return omControlEmployeesDataSubject;
    }

    public String getOmReleasePath() {
        return omReleasePath;
    }

    public String getOmReleaseSubject() {
        return omReleaseSubject;
    }

    public String getOmAdministrativePath() {
        return omAdministrativePath;
    }

    public String getOmAdministrativeSubject() {
        return omAdministrativeSubject;
    }

    public String getOmSalaryPath() {
        return omSalaryPath;
    }

    public String getOmSalarySubject() {
        return omSalarySubject;
    }

    public String getOmControlProjecttimesPath() {
        return omControlProjecttimesPath;
    }

    public String getOmControlProjecttimesSubject() {
        return omControlProjecttimesSubject;
    }
}
