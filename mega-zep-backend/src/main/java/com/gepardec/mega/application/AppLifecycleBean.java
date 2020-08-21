package com.gepardec.mega.application;

import io.agroal.api.AgroalDataSource;
import io.quarkus.runtime.Startup;
import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.ResourceAccessor;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import java.sql.SQLException;

@Startup
@ApplicationScoped
public class AppLifecycleBean {

    AppLifecycleBean(AgroalDataSource dataSource, @ConfigProperty(name = "quarkus.liquibase.change-log") String masterChangeLogFile) throws SQLException, LiquibaseException {
        ResourceAccessor resourceAccessor = new ClassLoaderResourceAccessor(AppLifecycleBean.class.getClassLoader());
        Liquibase liquibase = new Liquibase(masterChangeLogFile, resourceAccessor, new JdbcConnection(dataSource.getConnection()));
        liquibase.update(new Contexts());
    }
}
