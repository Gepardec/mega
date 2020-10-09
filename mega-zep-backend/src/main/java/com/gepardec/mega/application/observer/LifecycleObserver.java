package com.gepardec.mega.application.observer;

import com.gepardec.mega.application.configuration.ApplicationConfig;
import io.quarkus.runtime.StartupEvent;
import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.ResourceAccessor;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;
import javax.sql.DataSource;

/**
 * With Quarkus we need to inject in the observer method and not in the bean itself.
 */
@Dependent
public class LifecycleObserver {

    void initApplicationConfig(final @Observes StartupEvent event,
                               final ApplicationConfig applicationConfig) {
        applicationConfig.init();
    }

    void initDatabase(final @Observes StartupEvent event,
                      final DataSource dataSource,
                      final @ConfigProperty(name = "quarkus.liquibase.change-log") String masterChangeLogFile,
                      final Logger logger) {
        try {
            ResourceAccessor resourceAccessor = new ClassLoaderResourceAccessor(LifecycleObserver.class.getClassLoader());
            Liquibase liquibase = new Liquibase(masterChangeLogFile, resourceAccessor, new JdbcConnection(dataSource.getConnection()));
            liquibase.update(new Contexts());
            logger.info("Initialized database with liquibase");
        } catch (Exception e) {
            logger.error("Initialization of the database with liquibase failed", e);
        }
    }
}
