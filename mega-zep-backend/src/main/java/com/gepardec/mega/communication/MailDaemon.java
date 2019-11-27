package com.gepardec.mega.communication;

import com.gepardec.mega.zep.service.api.WorkerService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class MailDaemon {

    @Inject
    private MailSender mailSender;

    @Inject
    private WorkerService workerService;


    //    @Scheduled(every = "5s")
    void sendMailToEmployees() {


//        workerService.getAllEmployees()
//                .forEach(employee -> mailSender.sendMonthlyFriendlyReminder(employee.getVorname(), employee.getEmail()));


    }




}
