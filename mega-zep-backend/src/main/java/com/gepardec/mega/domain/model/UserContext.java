package com.gepardec.mega.domain.model;

import javax.annotation.Nullable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.*;
import lombok.experimental.*;
import lombok.extern.jackson.Jacksonized;

@Builder
@Getter
@ToString
@EqualsAndHashCode
@Accessors(fluent = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Jacksonized
public class UserContext {
    @Nullable
    private final User user;
}
