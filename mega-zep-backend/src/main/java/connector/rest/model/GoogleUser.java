package connector.rest.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoogleUser {
    private String id;
    private String email;
    private String name;
    private String firstName;
    private String lastName;
    private String photoUrl;
    private String authToken;
    private String idToken;
    private String authorizationCode;
    private String provider;
    private Object facebook;
    private Object linkedIn;
}
