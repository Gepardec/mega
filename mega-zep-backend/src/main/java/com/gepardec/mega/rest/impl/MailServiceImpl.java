package com.gepardec.mega.rest.impl;

import com.gepardec.mega.communication.MailSender;
import com.gepardec.mega.rest.api.MailService;

public class MailServiceImpl implements MailService {

   public String sendMail() {
        MailSender sender = new MailSender();
        sender.send();
        return "mail sent";
    }
}
