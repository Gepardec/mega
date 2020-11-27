package com.gepardec.mega.rest;

import com.gepardec.mega.db.entity.State;
import com.gepardec.mega.domain.model.Comment;
import com.gepardec.mega.domain.model.Role;
import com.gepardec.mega.domain.model.SecurityContext;
import com.gepardec.mega.domain.model.User;
import com.gepardec.mega.domain.model.UserContext;
import com.gepardec.mega.service.api.comment.CommentService;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.util.HashSet;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    void setCommentsStatusDone_whenUserNotLogged_thenReturnsHttpStatusUNAUTHORIZED() {
        final User user = createUserForRole(Role.EMPLOYEE);
        when(userContext.user()).thenReturn(user);

        given().contentType(ContentType.JSON).put("/comments/setdone")
                .then().assertThat().statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    @Test
    void setCommentsStatusDone_whenValid_thenReturnsUpdatedNumber() {
        when(commentService.setDone(ArgumentMatchers.any(Comment.class))).thenReturn(1);

        final User user = createUserForRole(Role.EMPLOYEE);
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

    private com.gepardec.mega.domain.model.User createUserForRole(final Role role) {
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        return com.gepardec.mega.domain.model.User.builder()
                .userId("1")
                .dbId(1)
                .email("thomas.herzog@gpeardec.com")
                .firstname("Thomas")
                .lastname("Herzog")
                .roles(roles)
                .build();
    }
}
