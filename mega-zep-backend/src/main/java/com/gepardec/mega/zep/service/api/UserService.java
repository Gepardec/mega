package com.gepardec.mega.zep.service.api;

import de.provantis.zep.MitarbeiterType;

public interface UserService {
    MitarbeiterType login(String token);
}
