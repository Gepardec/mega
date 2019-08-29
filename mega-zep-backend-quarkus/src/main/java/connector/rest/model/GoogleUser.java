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
    private Object facebook;
    private Object linkedIn;


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

    public void setId(String id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public Object getFacebook() {
        return facebook;
    }

    public void setFacebook(Object facebook) {
        this.facebook = facebook;
    }

    public Object getLinkedIn() {
        return linkedIn;
    }

    public void setLinkedIn(Object linkedIn) {
        this.linkedIn = linkedIn;
    }
}
