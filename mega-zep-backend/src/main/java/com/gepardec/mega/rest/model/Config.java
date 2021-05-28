package com.gepardec.mega.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.auto.value.AutoValue;

@AutoValue
@JsonSerialize(as = Config.class)
@JsonDeserialize(builder = Config.Builder.class)
public abstract class Config {

    public static Builder newBuilder() {
        return new com.gepardec.mega.rest.model.AutoValue_Config.Builder();
    }

    @JsonProperty
    public abstract String excelUrl();

    @JsonProperty
    public abstract String budgetCalculationExcelUrl();

    @JsonProperty
    public abstract String zepOrigin();

    @JsonProperty
    public abstract String clientId();

    @JsonProperty
    public abstract String issuer();

    @JsonProperty
    public abstract String scope();

    @JsonProperty
    public abstract String version();

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder excelUrl(String excelUrl);

        public abstract Builder budgetCalculationExcelUrl(String planrechnungUrl);

        public abstract Builder zepOrigin(String zepUrl);

        public abstract Builder clientId(String clientId);

        public abstract Builder issuer(String issuer);

        public abstract Builder scope(String scope);

        public abstract Builder version(String version);

        public abstract Config build();
    }
}
