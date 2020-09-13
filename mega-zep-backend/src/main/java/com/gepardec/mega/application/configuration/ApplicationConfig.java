package com.gepardec.mega.application.configuration;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

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

    @Inject
    @ConfigProperty(name = "quarkus.locales")
    List<Locale> locales;

    @Inject
    @ConfigProperty(name = "quarkus.default-locale") Locale defaultLocale;

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

    public List<Locale> getLocales() {
        return locales;
    }

    public Locale getDefaultLocale() {
        return defaultLocale;
    }
}
