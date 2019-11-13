package com.gepardec.mega.communication;


import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class MailSender {

    @Inject
    Mailer mailer;

    public void send() {
        System.out.println("start sending mail");

        Mail mail = Mail.withText("mario.brandmueller@gepardec.com", "My Mail from Quarkus", "He Bua, wos geht?");
        mailer.send(mail);
        System.out.println("mail sent");
    }


}
