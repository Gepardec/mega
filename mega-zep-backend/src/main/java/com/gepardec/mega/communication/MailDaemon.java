package com.gepardec.mega.communication;

import com.gepardec.mega.zep.service.api.WorkerService;
import io.quarkus.scheduler.Scheduled;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class MailDaemon {

    @Inject
    private MailSender mailSender;

    @Inject
    private WorkerService workerService;




    @Scheduled(every = "5s")
    void sendMailToEmployees() {
        System.out.println("5 seconds past");

        workerService.getAllEmployees()

    }




}
