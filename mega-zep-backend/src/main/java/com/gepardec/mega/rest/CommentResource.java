package com.gepardec.mega.rest;

import com.gepardec.mega.application.interceptor.Secured;
import com.gepardec.mega.domain.model.Comment;
import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.rest.model.NewCommentEntry;
import com.gepardec.mega.service.api.comment.CommentService;

import javax.inject.Inject;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Secured
@Path("/comments")
public class CommentResource {

    @Inject
    CommentService commentService;

    @PUT
    @Path("/setdone")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public int setDone(@NotNull(message = "{commentResource.comment.notNull}") final Comment comment) {
        return commentService.setDone(comment);
    }

    @GET
    @Path("/getallcommentsforemployee")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Comment> getAllCommentsForEmployee(
            @QueryParam("email") @NotNull(message = "{commentResource.email.notNull}") @Email(message = "{commentResource.email.invalid}") String employeeEmail,
            @QueryParam("date") @NotNull(message = "{commentResource.date.notNull}") String currentMonthYear) {
        return commentService.findCommentsForEmployee(Employee.builder().email(employeeEmail).releaseDate(currentMonthYear).build());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Comment newCommentForEmployee(@NotNull(message = "{commentResource.commentEntry.notNull}") NewCommentEntry newComment) {
        return commentService.createNewCommentForEmployee(
                newComment.stepId(),
                newComment.employee(),
                newComment.comment(),
                newComment.assigneeEmail(),
                newComment.project(),
                newComment.currentMonthYear()
        );
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public boolean deleteComment(@PathParam("id") @NotNull(message = "{commentResource.id.notNull}") Long id) {
        return commentService.deleteCommentWithId(id);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Comment updateCommentForEmployee(@NotNull(message = "{commentResource.comment.notNull}") Comment comment) {
        return commentService.updateComment(comment.id(), comment.message());
    }

}
