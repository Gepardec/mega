package com.gepardec.mega.zep.service.impl;

import com.gepardec.mega.security.SessionUser;
import com.gepardec.mega.security.UnauthorizedException;
import com.gepardec.mega.zep.service.api.AuthenticationService;
import com.gepardec.mega.zep.soap.ZepSoapProvider;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import de.provantis.zep.MitarbeiterType;
import de.provantis.zep.ReadMitarbeiterRequestType;
import de.provantis.zep.ReadMitarbeiterResponseType;
import de.provantis.zep.ZepSoapPortType;
import org.slf4j.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.security.GeneralSecurityException;

@RequestScoped
public class AuthenticationServiceImpl implements AuthenticationService {

    @Inject
    Logger logger;

    @Inject
    ZepSoapPortType zepSoapPortType;

    @Inject
    GoogleIdTokenVerifier tokenVerifier;

    @Inject
    ZepSoapProvider zepSoapProvider;

    @Inject
    SessionUser sessionUser;

    @Override
    public MitarbeiterType login(String email, String idToken) {
        if (sessionUser.isLoggedIn()) {
            return null;
        }

        try {
            tokenVerifier.verify(idToken);
        } catch (GeneralSecurityException e) {
            throw new UnauthorizedException(String.format("IdToken of user '%s' is invalid", email));
        } catch (Exception e) {
            throw new IllegalStateException(String.format("Could not verify idToken for user '%s'", email));
        }

        // get ZEP employee
        final ReadMitarbeiterRequestType empl = new ReadMitarbeiterRequestType();
        empl.setRequestHeader(zepSoapProvider.createRequestHeaderType());

        final ReadMitarbeiterResponseType rmrt = zepSoapPortType.readMitarbeiter(empl);
        //TODO: contact ZEP for enabling search-criteria for mail-address
        final MitarbeiterType mt = rmrt.getMitarbeiterListe().getMitarbeiter().stream()
                .filter(emp -> email.equals(emp.getEmail()))
                .findFirst().orElse(null);

        // invalidate session when theres no appropriate employee
        if (mt == null) {
            throw new SecurityException(String.format("employee with id %s not found in ZEP", email));
        }

        sessionUser.init(email, idToken, mt.getRechte());

        logger.info("User '{}' logged in", email);
        return mt;
    }
}
