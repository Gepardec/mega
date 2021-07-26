package com.gepardec.mega.service.api.init;

import java.time.LocalDate;

public interface EnterpriseSyncService {

    boolean generateEnterpriseEntries();

    boolean generateEnterpriseEntries(LocalDate date);
}
