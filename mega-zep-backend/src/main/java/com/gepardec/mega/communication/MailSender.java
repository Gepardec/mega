package com.gepardec.mega.communication;


import com.gepardec.mega.model.google.GoogleUser;
import io.quarkus.arc.config.ConfigProperties;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
@ConfigProperties()
public class MailSender {

    @Inject
    Mailer mailer;

    public void send(List<GoogleUser> googleUsers, ) {
        System.out.println("start sending mail");

        Mail mail = Mail.withText("mario.brandmueller@gepardec.com", "My Mail from Quarkus", "He Bua, wos geht?");
        mailer.send(mail);
        System.out.println("mail sent");
    }
}
