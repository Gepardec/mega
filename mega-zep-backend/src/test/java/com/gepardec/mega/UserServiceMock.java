package com.gepardec.mega;

import com.gepardec.mega.aplication.security.Role;
import com.gepardec.mega.aplication.security.SessionUser;
import com.gepardec.mega.zep.service.impl.UserServiceImpl;
import de.provantis.zep.MitarbeiterType;
import io.quarkus.test.Mock;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@Mock
@ApplicationScoped
public class UserServiceMock extends UserServiceImpl {

    @Inject
    private SessionUser sessionUser;

    private MitarbeiterType mitarbeiter;

    @Override
    public MitarbeiterType login(String idToken) {
        sessionUser.init(mitarbeiter.getUserId(), mitarbeiter.getEmail(), idToken, mitarbeiter.getRechte());

        return mitarbeiter;
    }

    public static MitarbeiterType createMitarbeiterType() {
        final MitarbeiterType mitarbeiter = new MitarbeiterType();
        mitarbeiter.setUserId("1337-testvorname.testnachname");
        mitarbeiter.setEmail("test@gepardec.com");
        mitarbeiter.setVorname("Testvorname");
        mitarbeiter.setNachname("Testnachname");
        mitarbeiter.setRechte(Role.USER.roleId);

        return mitarbeiter;
    }

    public MitarbeiterType getMitarbeiter() {
        return mitarbeiter;
    }

    public void setMitarbeiter(MitarbeiterType mitarbeiter) {
        this.mitarbeiter = mitarbeiter;
    }
}
