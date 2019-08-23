package connector.rest.model;

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

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getIdToken() {
        return idToken;
    }

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public String getProvider() {
        return provider;
    }
}
