package com.gepardec.mega.monthlyreport.warning;

import lombok.Getter;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@Getter
public class WarningConfig {
    @ConfigProperty(name = "warning.time.excess_worktime")
    String excessWorktime;
    @ConfigProperty(name = "warning.time.missing_breaktime")
    String missingBreaktime;
    @ConfigProperty(name = "warning.time.missing_resttime")
    String missingResttime;
    @ConfigProperty(name = "warning.time.too_early_start")
    String tooEarlyStart;
    @ConfigProperty(name = "warning.time.too_late_end")
    String tooLateEnd;
    @ConfigProperty(name = "warning.journey.missing_journey_back")
    String missingJourneyBack;
    @ConfigProperty(name = "warning.journey.missing_journey_to_aim")
    String missingJourneyToAim;
}