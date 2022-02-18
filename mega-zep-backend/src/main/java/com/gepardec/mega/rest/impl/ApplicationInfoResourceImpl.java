package com.gepardec.mega.rest.impl;

import com.gepardec.mega.application.configuration.ApplicationConfig;
import com.gepardec.mega.rest.api.ApplicationInfoResource;
import com.gepardec.mega.rest.model.ApplicationInfoDto;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.time.Duration;
import java.time.LocalDateTime;

@RequestScoped
public class ApplicationInfoResourceImpl implements ApplicationInfoResource {

    @Inject
    ApplicationConfig applicationConfig;

    @Override
    public ApplicationInfoDto get() {
        final Duration upTime = Duration.between(applicationConfig.getStartAt(), LocalDateTime.now());

        return ApplicationInfoDto.builder()
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
