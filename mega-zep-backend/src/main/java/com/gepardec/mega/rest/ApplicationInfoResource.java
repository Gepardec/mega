package com.gepardec.mega.rest;

import com.gepardec.mega.application.configuration.ApplicationConfig;
import com.gepardec.mega.rest.model.ApplicationInfo;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.time.Duration;
import java.time.LocalDateTime;

@RequestScoped
public class ApplicationInfoResource implements ApplicationInfoResourceAPI {

    @Inject
    ApplicationConfig applicationConfig;

    @Override
    public ApplicationInfo get() {
        final Duration upTime = Duration.between(applicationConfig.getStartAt(), LocalDateTime.now());
        return ApplicationInfo.builder()
                .version(applicationConfig.getVersion())
                .buildDate(applicationConfig.getBuildDate())
                .buildNumber(applicationConfig.getBuildNumber())
                .commit(applicationConfig.getCommit())
                .branch(applicationConfig.getBranch())
                .startedAt(applicationConfig.getStartAt())
                .upTime(upTime)
                .build();
    }
}
