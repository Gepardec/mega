package com.gepardec.mega;

import com.gepardec.mega.zep.service.impl.UserServiceImpl;
import de.provantis.zep.MitarbeiterType;
import io.quarkus.test.Mock;

import javax.enterprise.context.ApplicationScoped;

@Mock
@ApplicationScoped
public class UserServiceMock extends UserServiceImpl {

    @Override
    public MitarbeiterType login(String idToken) {
        return new MitarbeiterType();
    }
}
