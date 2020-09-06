package com.gepardec.mega.application.configuration;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.LocalDateTime;

@ApplicationScoped
public class ApplicationConfig {

    @Inject
    @ConfigProperty(name = "mega.info.build.version")
    String version;

    @Inject
    @ConfigProperty(name = "mega.info.build.date")
    LocalDateTime buildDate;

    @Inject
    @ConfigProperty(name = "mega.info.build.number")
    Integer buildNumber;

    @Inject
    @ConfigProperty(name = "mega.info.git.commit")
    String commit;

    @Inject
    @ConfigProperty(name = "mega.info.git.branch")
    String branch;

    private final LocalDateTime startAt = LocalDateTime.now();

    public String getVersion() {
        return version;
    }

    public LocalDateTime getBuildDate() {
        return buildDate;
    }

    public Integer getBuildNumber() {
        return buildNumber;
    }

    public String getCommit() {
        return commit;
    }

    public String getBranch() {
        return branch;
    }

    public LocalDateTime getStartAt() {
        return startAt;
    }
}
