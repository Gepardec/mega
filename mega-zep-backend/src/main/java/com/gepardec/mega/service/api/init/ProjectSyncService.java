package com.gepardec.mega.service.api.init;

import java.time.LocalDate;

public interface ProjectSyncService {

    boolean generateProjects();

    boolean generateProjects(LocalDate date);
}
