package com.gepardec.mega.rest.impl;

import com.gepardec.mega.notification.mail.Mail;
import com.gepardec.mega.notification.mail.MailParameter;
import com.gepardec.mega.notification.mail.MailSender;
import com.gepardec.mega.rest.api.TestResource;
import com.gepardec.mega.service.api.EnterpriseSyncService;
import com.gepardec.mega.service.api.ProjectSyncService;
import com.gepardec.mega.service.api.StepEntrySyncService;
import io.quarkus.arc.properties.IfBuildProperty;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

// The property 'mega.endpoint.test.enable' is set to 'true' during CI/CD builds.
@RequestScoped
@IfBuildProperty(name = "mega.endpoint.test.enable", stringValue = "true", enableIfMissing = true)
public class TestResourceImpl implements TestResource {

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
                .generateStepEntriesFromEndpoint();
        return Response.ok("ok").build();
    }

    @Override
    public Response mailComment() {
        Optional.ofNullable(mailSender)
                .orElseThrow(() -> new IllegalStateException("TestResource is disabled and 'MailSender' is null"))
                .send(Mail.COMMENT_CREATED,
                        "no-reply@gepardec.com",
                        "Max",
                        Locale.GERMAN,
                        Map.of(MailParameter.CREATOR, "Max",
                                MailParameter.RECIPIENT, "Max",
                                MailParameter.COMMENT, "This is my comment"),
                        List.of("Max"));
        mailSender.send(Mail.COMMENT_CLOSED,
                "no-reply@gepardec.com",
                "Max",
                Locale.GERMAN,
                Map.of(MailParameter.CREATOR, "Max",
                        MailParameter.RECIPIENT, "Max",
                        MailParameter.COMMENT, "This is my comment"),
                List.of("Max"));
        return Response.ok("ok").build();
    }
}
