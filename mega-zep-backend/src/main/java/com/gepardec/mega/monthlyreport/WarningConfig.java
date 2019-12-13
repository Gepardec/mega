package com.gepardec.mega.monthlyreport;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
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


    public String getTextByWarning(Warning warning) {
        String warningText;
        switch (warning) {
            case WARNING_EXCESS_WORKTIME: {
                warningText = excessWorktime;
                break;
            }
            case WARNING_MISSING_BREAKTIME: {
                warningText = missingBreaktime;
                break;
            }
            case WARNING_TIME_TOO_EARLY_START: {
                warningText = tooEarlyStart;
                break;
            }
            case WARNING_TIME_TOO_LATE_END: {
                warningText = tooLateEnd;
                break;
            }
            case WARNING_MISSING_RESTTIME: {
                warningText = missingResttime;
                break;
            }
            case WARNING_JOURNEY_BACK_MISSING: {
                warningText = missingJourneyBack;
                break;
            }
            case WARNING_JOURNEY_TO_AIM_MISSING: {
                warningText = missingJourneyToAim;
                break;
            }

            default: {
                warningText = null;
                break;
            }
        }
        return warningText;
    }
}
