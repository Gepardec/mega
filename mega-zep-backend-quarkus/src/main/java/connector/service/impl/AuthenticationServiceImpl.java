package connector.service.impl;

import connector.rest.model.GoogleUser;
import connector.security.AuthorizationInterceptor;
import connector.security.SessionUser;
import connector.service.api.AuthenticationService;
import de.provantis.zep.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

@ApplicationScoped
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    @Inject
    ZepSoapPortType zepSoapPortType;

    @Inject
    RequestHeaderType requestHeaderType;

    @Inject
    SessionUser sessionUser;

    @Override
    public Response login(GoogleUser user, HttpServletRequest request) {
        if (isUserLoggedIn()) {
            return Response.ok(user).build();
        }

        ReadMitarbeiterRequestType empl = new ReadMitarbeiterRequestType();
        System.out.print("Empl: ");
        System.out.println(empl);
        empl.setRequestHeader(requestHeaderType);



        ReadMitarbeiterResponseType rmrt = zepSoapPortType.readMitarbeiter(empl);
        System.out.print("rmrt: ");
        System.out.println(rmrt);
        MitarbeiterType mt = rmrt.getMitarbeiterListe().getMitarbeiter().stream()
                .filter(emp -> user.getEmail().equals(emp.getEmail()))
                .findFirst()
                .orElse(null);

        if(mt == null){
            invalidateSession(request);
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        System.out.println("BEFORE request.login");
        try {
            System.out.println(request.getQueryString());
            System.out.println(user.getEmail());
            System.out.println(user.getIdToken());
            request.getSession();
            System.out.println("AFTER request.login");
            LOG.info("Authentication of user with name " + user.getName());
            sessionUser.setId(user.getId());
            sessionUser.setAuthorizationCode(user.getAuthorizationCode());
            sessionUser.setEmail(user.getEmail());
            sessionUser.setAuthToken(user.getAuthToken());
            sessionUser.setIdToken(user.getIdToken());
            sessionUser.setName(user.getName());
            sessionUser.setRole(mt.getRechte());
        } catch (Exception e){
            LOG.info("Authentication of user with name " + user.getName() + " failed: " + e);
            invalidateSession(request);
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        LOG.info("Authentication of user with name " + user.getName() + " successful");
        return Response.ok(user).build();
    }

    @Override
    public Response logout(HttpServletRequest request) {
        invalidateSession(request);
        return Response.ok().build();
    }

    private boolean isUserLoggedIn() {
        return sessionUser.getId() != null;
    }

    private void invalidateSession(HttpServletRequest request){
        request.getSession().invalidate();
        sessionUser.invalidate();
    }
}
