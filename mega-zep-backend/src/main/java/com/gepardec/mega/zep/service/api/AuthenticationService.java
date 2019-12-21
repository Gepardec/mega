package com.gepardec.mega.zep.service.api;

import de.provantis.zep.MitarbeiterType;

public interface AuthenticationService {
    MitarbeiterType login(String email, String token);
}
