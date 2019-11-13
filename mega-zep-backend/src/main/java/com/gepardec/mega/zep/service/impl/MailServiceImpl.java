package com.gepardec.mega.zep.service.impl;

import com.gepardec.mega.communication.MailSender;
import com.gepardec.mega.model.google.GoogleUser;
import com.gepardec.mega.zep.service.api.MailService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

public class MailServiceImpl implements MailService {

    @GET
    @Path("user/mail")
    public boolean sendMail(GoogleUser user) {
        MailSender sender = new MailSender();
        sender.send();
        return true;
    }
}
