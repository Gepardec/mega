package com.gepardec.mega.service.api.init;

import java.time.LocalDate;

public interface StepEntrySyncService {

    void generateStepEntries();

    void generateStepEntries(LocalDate date);
}
