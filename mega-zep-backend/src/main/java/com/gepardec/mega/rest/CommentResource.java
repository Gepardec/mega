package com.gepardec.mega.rest;

import com.gepardec.mega.application.interceptor.Secured;
import com.gepardec.mega.domain.model.Comment;
import com.gepardec.mega.service.api.comment.CommentService;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Secured
@Path("/comments")
public class CommentResource {

    @Inject
    CommentService commentService;

    @PUT
    @Path("/setdone")
    @Produces(MediaType.APPLICATION_JSON)
    public int setDone(@NotNull(message = "{commentResource.comment.notNull}") final Comment comment) {
        return commentService.setCommentStatusDone(comment);
    }
}
