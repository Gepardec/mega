package com.gepardec.mega.zep.service.api;

import com.gepardec.mega.model.google.GoogleUser;

public interface MailService {
    boolean sendMail(GoogleUser user);
}
