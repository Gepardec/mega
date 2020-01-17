package com.gepardec.mega.rest.model;

public class Config {

    private String oauthClientId;

    public Config() {
    }

    public Config(String oauthClientId) {
        this.oauthClientId = oauthClientId;
    }

    public String getOauthClientId() {
        return oauthClientId;
    }

    public void setOauthClientId(String oauthClientId) {
        this.oauthClientId = oauthClientId;
    }
}
