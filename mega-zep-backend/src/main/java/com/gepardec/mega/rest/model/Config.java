package com.gepardec.mega.rest.model;

public class Config {

    private final String clientId;
    private final String issuer;
    private final String scope;

    public Config(String clientId, String issuer, String scope) {
        this.clientId = clientId;
        this.issuer = issuer;
        this.scope = scope;
    }

    public String getClientId() {
        return clientId;
    }

    public String getIssuer() {
        return issuer;
    }

    public String getScope() {
        return scope;
    }
}
