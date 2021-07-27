package com.gepardec.mega.rest;

import com.gepardec.mega.notification.mail.Mail;
import com.gepardec.mega.notification.mail.MailParameter;
import com.gepardec.mega.notification.mail.MailSender;
import com.gepardec.mega.service.api.init.EnterpriseSyncService;
import com.gepardec.mega.service.api.init.ProjectSyncService;
import com.gepardec.mega.service.api.init.StepEntrySyncService;
import io.quarkus.arc.properties.IfBuildProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

// The property 'mega.endpoint.test.enable' is set to 'true' during CI/CD builds.
@ApplicationScoped
public class TestResource implements TestResourceAPI {

    @Inject
    StepEntrySyncService stepEntrySyncService;

    @Inject
    ProjectSyncService projectSyncService;

    @Inject
    EnterpriseSyncService enterpriseSyncService;

    @Inject
    MailSender mailSender;

    @Override
    public Response syncProjects() {
        return Response.ok(projectSyncService.generateProjects()).build();
    }

    @Override
    public Response syncEnterpriseEntries() {
        return Response.ok(enterpriseSyncService.generateEnterpriseEntries()).build();
    }

    @Override
    public Response synctest() {
        Optional.ofNullable(stepEntrySyncService)
                .orElseThrow(() -> new IllegalStateException("TestResource is disabled and 'StepEntrySyncService' is null"))
                .generateStepEntries();
        return Response.ok("ok").build();
    }

    @Override
    public Response mailComment() {
        Optional.ofNullable(mailSender)
                .orElseThrow(() -> new IllegalStateException("TestResource is disabled and 'MailSender' is null"))
                .send(Mail.COMMENT_CREATED,
                        "thomas.herzog@gepardec.com",
                        "Thomas",
                        Locale.GERMAN,
                        Map.of(MailParameter.CREATOR, "Werner",
                                MailParameter.RECIPIENT, "Thomas",
                                MailParameter.COMMENT, "This is my comment"),
                        List.of("Thomas"));
        mailSender.send(Mail.COMMENT_CLOSED,
                "thomas.herzog@gepardec.com",
                "Thomas",
                Locale.GERMAN,
                Map.of(MailParameter.CREATOR, "Werner",
                        MailParameter.RECIPIENT, "Thomas",
                        MailParameter.COMMENT, "This is my comment"),
                List.of("Thomas"));
        return Response.ok("ok").build();
    }
}
