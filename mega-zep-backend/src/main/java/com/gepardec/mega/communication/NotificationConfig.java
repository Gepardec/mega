package com.gepardec.mega.communication;

import lombok.Getter;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@Getter
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



}
