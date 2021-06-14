package com.gepardec.mega.rest;

import com.gepardec.mega.notification.mail.Mail;
import com.gepardec.mega.notification.mail.MailParameter;
import com.gepardec.mega.notification.mail.MailSender;
import com.gepardec.mega.service.api.init.ProjectSyncService;
import com.gepardec.mega.service.api.init.StepEntrySyncService;
import io.quarkus.arc.properties.IfBuildProperty;

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
@IfBuildProperty(name = "mega.endpoint.test.enable", stringValue = "true", enableIfMissing = true)
@RequestScoped
@Path("/test/")
public class TestResource {

    @Inject
    StepEntrySyncService stepEntrySyncService;

    @Inject
    ProjectSyncService projectSyncService;

    @Inject
    MailSender mailSender;

    @Path("/sync/projects")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response syncProjects() {
        return Response.ok(projectSyncService.generateProjects()).build();
    }

    @Path("/sync")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response synctest() {
        Optional.ofNullable(stepEntrySyncService)
                .orElseThrow(() -> new IllegalStateException("TestResource is disabled and 'StepEntrySyncService' is null"))
                .genereteStepEntries();
        return Response.ok("ok").build();
    }

    @Path("/mail-comment")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
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
