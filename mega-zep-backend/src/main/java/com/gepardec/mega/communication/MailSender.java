package com.gepardec.mega.communication;


import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class MailSender {

    @Inject
    private Mailer mailer;




//    megagepardec@gmail.com
//    Gepard@123

    public static void main(String args[]) {


        MailSender sender = new MailSender();
        sender.send();
//        String sender = "megagepardec@gmail.com";
//        String receiver = "mario.brandmueller@gepardec.com";
//        String subject = "MEGA - Monatsendauswertung";
//        String msg = "He, Bua!";
//
//
//        send(sender, "Gepard@123", receiver, subject, msg);
    }


    public void send() {
        System.out.println("start sending mail");

        mailer.send(Mail.withText("mario.brandmueller@gepardec.com", "My Mail from Quarkus", "He Bua, wos geht?"));
        System.out.println("mail sent");
    }


}
