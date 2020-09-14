package com.gepardec.mega.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.auto.value.AutoValue;

@AutoValue
@JsonSerialize(as = Config.class)
@JsonDeserialize(builder = Config.Builder.class)
public abstract class Config {

    @JsonProperty
    public abstract String clientId();

    @JsonProperty
    public abstract String issuer();

    @JsonProperty
    public abstract String scope();

    @JsonProperty
    public abstract String version();

    public static Builder newBuilder() {
        return new com.gepardec.mega.rest.model.AutoValue_Config.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder clientId(String clientId);

        public abstract Builder issuer(String issuer);

        public abstract Builder scope(String scope);

        public abstract Builder version(String version);

        public abstract Config build();
    }
}
