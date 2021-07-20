package com.gepardec.mega.rest;

import com.gepardec.mega.application.configuration.ApplicationConfig;
import com.gepardec.mega.rest.model.ApplicationInfo;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.time.Duration;
import java.time.LocalDateTime;

@Path("/info")
public class ApplicationInfoResource {

    @Inject
    ApplicationConfig applicationConfig;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
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
