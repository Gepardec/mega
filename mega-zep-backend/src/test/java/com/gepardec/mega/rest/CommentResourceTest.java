package com.gepardec.mega.rest;

import com.gepardec.mega.db.entity.State;
import com.gepardec.mega.domain.model.*;
import com.gepardec.mega.rest.model.NewCommentEntry;
import com.gepardec.mega.service.api.comment.CommentService;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@QuarkusTest
public class CommentResourceTest {

    @InjectMock
    private UserContext userContext;

    @InjectMock
    private SecurityContext securityContext;

    @InjectMock
    private CommentService commentService;

    @Test
    void setCommentStatusDone_whenPOST_thenReturnsStatusMETHOD_NOT_ALLOWED() {
        given().contentType(ContentType.JSON)
                .post("/comments/setdone")
                .then().statusCode(HttpStatus.SC_METHOD_NOT_ALLOWED);
    }

    @Test
    void setDone_whenUserNotLogged_thenReturnsHttpStatusUNAUTHORIZED() {
        final User user = createUserForRole(Role.USER);
        when(userContext.user()).thenReturn(user);

        given().contentType(ContentType.JSON).put("/comments/setdone")
                .then().assertThat().statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    @Test
    void setDone_whenValid_thenReturnsUpdatedNumber() {
        when(commentService.setDone(ArgumentMatchers.any(Comment.class))).thenReturn(1);

        final User user = createUserForRole(Role.USER);
        when(securityContext.email()).thenReturn(user.email());
        when(userContext.user()).thenReturn(user);

        Comment comment = Comment.builder()
                .id(0L)
                .message("Pausen eintragen!")
                .author("evelyn.pirklbauer@gepardec.com")
                .state(State.IN_PROGRESS)
                .build();

        final int updatedCount = given().contentType(ContentType.JSON)
                .body(comment)
                .put("/comments/setdone")
                .as(Integer.class);

        assertEquals(1, updatedCount);
    }

    @Test
    void getAllCommentsForEmployee_whenMethodPOST_thenReturnsStatusMETHOD_NOT_ALLOWED() {
        given().contentType(ContentType.JSON)
                .queryParam("email", "marko.gattringer@gmx.at")
                .queryParam("releasedate", "2020-10-01")
                .post("/comments/getallcommentsforemployee")
                .then().assertThat().statusCode(HttpStatus.SC_METHOD_NOT_ALLOWED);
    }

    @Test
    void getAllCommentsForEmployee_whenNotLogged_thenReturnsHttpStatusUNAUTHORIZED() {
        final User user = createUserForRole(Role.USER);
        when(userContext.user()).thenReturn(user);

        given().contentType(ContentType.JSON)
                .queryParam("email", "marko.gattringer@gmx.at")
                .queryParam("releasedate", "2020-10-01")
                .get("/comments/getallcommentsforemployee")
                .then().assertThat().statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    @Test
    void getAllCommentsForEmployee_whenInvalidEmail_thenReturnsHttpStatusBAD_REQUEST() {
        final User user = createUserForRole(Role.USER);
        when(securityContext.email()).thenReturn(user.email());
        when(userContext.user()).thenReturn(user);

        given().contentType(ContentType.JSON)
                .queryParam("email", "marko.gattringergmx.at")
                .queryParam("releasedate", "2020-10-01")
                .get("/comments/getallcommentsforemployee")
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    void getAllCommentsForEmployee_whenEmailIsMissing_thenReturnsHttpStatusBAD_REQUEST() {
        final User user = createUserForRole(Role.USER);
        when(securityContext.email()).thenReturn(user.email());
        when(userContext.user()).thenReturn(user);

        given().contentType(ContentType.JSON)
                .queryParam("releasedate", "2020-10-01")
                .get("/comments/getallcommentsforemployee")
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    void getAllCommentsForEmployee_whenReleaseDateIsMissing_thenReturnsHttpStatusBAD_REQUEST() {
        final User user = createUserForRole(Role.USER);
        when(securityContext.email()).thenReturn(user.email());
        when(userContext.user()).thenReturn(user);

        given().contentType(ContentType.JSON)
                .queryParam("email", "marko.gattringer@gmx.at")
                .get("/comments/getallcommentsforemployee")
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    void getAllCommentsForEmployee_whenValid_thenReturnsListOfCommentsForEmployee() {
        final User user = createUserForRole(Role.USER);
        when(securityContext.email()).thenReturn(user.email());
        when(userContext.user()).thenReturn(user);

        Comment comment = Comment.builder().id(0L).message("Pausen eintragen!").author("evelyn.pirklbauer@gepardec.com").state(State.IN_PROGRESS).build();
        when(commentService.findCommentsForEmployee(ArgumentMatchers.any(Employee.class)))
                .thenReturn(List.of(comment));

        List<Comment> comments = given().contentType(ContentType.JSON)
                .queryParam("email", "marko.gattringer@gmx.at")
                .queryParam("releasedate", "2020-10-01")
                .get("/comments/getallcommentsforemployee")
                .as(new TypeRef<>() {
                });

        assertEquals(1L, comments.size());
        assertEquals(comment, comments.get(0));
    }

    @Test
    void newCommentForEmployee_whenNotLogged_thenReturnsHttpStatusUNAUTHORIZED() {
        given().contentType(ContentType.JSON)
                .post("/comments")
                .then().assertThat().statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    @Test
    void newCommentForEmployee_whenInvalidRequest_thenReturnsHttpStatusBAD_REQUEST() {
        final User user = createUserForRole(Role.USER);
        when(securityContext.email()).thenReturn(user.email());
        when(userContext.user()).thenReturn(user);

        given().contentType(ContentType.JSON)
                .post("/comments")
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST);
    }


    @Test
    void newCommentForEmployee_whenValid_thenReturnsCreatedComment() {
        final User user = createUserForRole(Role.USER);
        when(securityContext.email()).thenReturn(user.email());
        when(userContext.user()).thenReturn(user);

        when(commentService.createNewCommentForEmployee(ArgumentMatchers.anyLong(), ArgumentMatchers.any(Employee.class), ArgumentMatchers.anyString()))
                .thenReturn(Comment.builder().message("Pausen eintragen!").build());

        Employee employee = Employee.builder().build();
        NewCommentEntry newCommentEntry = NewCommentEntry.builder().comment("Pausen eintragen!").employee(employee).stepId(2L).build();
        Comment createdComment = given().contentType(ContentType.JSON)
                .body(newCommentEntry)
                .post("/comments")
                .as(Comment.class);

        assertNotNull(createdComment);
        assertEquals(newCommentEntry.comment(), createdComment.message());
    }

    @Test
    void deleteComment_whenIdIsMissing_thenReturnsHttpStatusMethodNotAllowed() {
        given().contentType(ContentType.JSON)
                .delete("/comments")
                .then().assertThat().statusCode(HttpStatus.SC_METHOD_NOT_ALLOWED);
    }

    @Test
    void deleteComment_whenNotLogged_thenReturnsHttpStatusUNAUTHORIZED() {
        given().contentType(ContentType.JSON)
                .delete("/comments/1")
                .then().assertThat().statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    @Test
    void deleteComment_whenValid_thenReturnsTrue() {
        final User user = createUserForRole(Role.USER);
        when(securityContext.email()).thenReturn(user.email());
        when(userContext.user()).thenReturn(user);

        when(commentService.deleteCommentWithId(ArgumentMatchers.anyLong()))
                .thenReturn(Boolean.TRUE);

        Boolean result = given().contentType(ContentType.JSON)
                .delete("/comments/1")
                .as(Boolean.class);

        assertEquals(result, Boolean.TRUE);
    }

    @Test
    void updateCommentForEmployee_whenNotLogged_thenReturnsHttpStatusUNAUTHORIZED() {
        given().contentType(ContentType.JSON)
                .put("/comments")
                .then().assertThat().statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    @Test
    void updateCommentForEmployee_whenInvalidRequest_henReturnsHttpStatusBAD_REQUEST() {
        final User user = createUserForRole(Role.USER);
        when(securityContext.email()).thenReturn(user.email());
        when(userContext.user()).thenReturn(user);

        given().contentType(ContentType.JSON)
                .put("/comments")
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    void updateCommentForEmployee_whenValid_thenReturnsUpdatedComment() {
        final User user = createUserForRole(Role.USER);
        when(securityContext.email()).thenReturn(user.email());
        when(userContext.user()).thenReturn(user);

        Comment comment = Comment.builder().id(1L).message("Zeiten prüfen").build();
        when(commentService.updateComment(ArgumentMatchers.anyLong(), ArgumentMatchers.anyString()))
                .thenReturn(comment);

        Comment updatedComment = given().contentType(ContentType.JSON)
                .body(comment)
                .put("/comments")
                .as(Comment.class);

        assertEquals(comment, updatedComment);
    }

    private com.gepardec.mega.domain.model.User createUserForRole(final Role role) {
        return com.gepardec.mega.domain.model.User.builder()
                .userId("1")
                .dbId(1)
                .email("thomas.herzog@gpeardec.com")
                .firstname("Thomas")
                .lastname("Herzog")
                .role(role)
                .build();
    }
}
