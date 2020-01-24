package com.gepardec.mega.rest.model;

public class Config {

    private final String oauthClientId;
    private final String issuer;
    private final String scope;
    
    public Config(String oauthClientId, String issuer, String scope) {
        this.oauthClientId = oauthClientId;
        this.issuer = issuer;
        this.scope = scope;
    }

    public String getOauthClientId() {
        return oauthClientId;
    }

    public String getIssuer() {
        return issuer;
    }

    public String getScope() {
        return scope;
    }
}
