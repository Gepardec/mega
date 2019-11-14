package com.gepardec.mega.zep.service.api;

import com.gepardec.mega.model.google.GoogleUser;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

public interface AuthenticationService {
    Response login (GoogleUser user, HttpServletRequest request);
    Response logout(HttpServletRequest request);
}
