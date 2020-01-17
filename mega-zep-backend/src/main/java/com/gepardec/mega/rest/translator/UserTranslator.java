package com.gepardec.mega.rest.translator;

import com.gepardec.mega.rest.model.User;
import com.gepardec.mega.aplication.security.Role;
import de.provantis.zep.MitarbeiterType;

public class UserTranslator {

    private UserTranslator() {
    }

    public static User translate(final MitarbeiterType mitarbeiter) {
        final User user = new User();
        user.setEmail(mitarbeiter.getEmail());
        user.setFirstname(mitarbeiter.getVorname());
        user.setLastname(mitarbeiter.getNachname());
        user.setRole(Role.forId(mitarbeiter.getRechte()).orElse(null));

        return user;
    }
}
