package com.gepardec.mega.rest.api;

import com.gepardec.mega.domain.model.Comment;
import com.gepardec.mega.rest.model.CommentDto;
import com.gepardec.mega.rest.model.NewCommentEntryDto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/comments")
public interface CommentResource {
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/setdone")
    Response setDone(@NotNull(message = "{commentResource.comment.notNull}") CommentDto commentDto);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getallcommentsforemployee")
    Response getAllCommentsForEmployee(
            @QueryParam("email") @NotNull(message = "{commentResource.email.notNull}") @Email(message = "{commentResource.email.invalid}") String employeeEmail,
            @QueryParam("date") @NotNull(message = "{commentResource.date.notNull}") String currentMonthYear);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response newCommentForEmployee(@NotNull(message = "{commentResource.commentEntry.notNull}") NewCommentEntryDto newComment);

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    Response deleteComment(@PathParam("id") @NotNull(message = "{commentResource.id.notNull}") Long id);

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response updateCommentForEmployee(@NotNull(message = "{commentResource.comment.notNull}") Comment commentDto);
}
