package com.gepardec.mega.rest;

import com.gepardec.mega.application.interceptor.Secured;
import com.gepardec.mega.domain.model.Comment;
import com.gepardec.mega.rest.model.NewCommentEntry;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Secured
@Path("/comments")
public interface CommentResourceAPI {
    @PUT
    @Path("/setdone")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    int setDone(@NotNull(message = "{commentResource.comment.notNull}") Comment comment);

    @GET
    @Path("/getallcommentsforemployee")
    @Produces(MediaType.APPLICATION_JSON)
    List<Comment> getAllCommentsForEmployee(
            @QueryParam("email") @NotNull(message = "{commentResource.email.notNull}") @Email(message = "{commentResource.email.invalid}") String employeeEmail,
            @QueryParam("date") @NotNull(message = "{commentResource.date.notNull}") String currentMonthYear);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Comment newCommentForEmployee(@NotNull(message = "{commentResource.commentEntry.notNull}") NewCommentEntry newComment);

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    boolean deleteComment(@PathParam("id") @NotNull(message = "{commentResource.id.notNull}") Long id);

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Comment updateCommentForEmployee(@NotNull(message = "{commentResource.comment.notNull}") Comment comment);
}
