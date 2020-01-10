package com.gepardec.mega;

import com.gepardec.mega.zep.service.impl.AuthenticationServiceImpl;
import de.provantis.zep.MitarbeiterType;
import io.quarkus.test.Mock;

import javax.enterprise.context.ApplicationScoped;

@Mock
@ApplicationScoped
public class AuthenticationServiceMock extends AuthenticationServiceImpl {

    @Override
    public MitarbeiterType login(String idToken) {
        return new MitarbeiterType();
    }
}
