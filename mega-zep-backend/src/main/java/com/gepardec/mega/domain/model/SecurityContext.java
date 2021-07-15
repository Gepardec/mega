package com.gepardec.mega.domain.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.annotation.Nullable;

import lombok.*;
import lombok.experimental.Accessors;
import lombok.extern.jackson.Jacksonized;



@Builder
@Getter
@ToString
@EqualsAndHashCode
@Accessors(fluent = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Jacksonized
public class SecurityContext {
    @Nullable
    private final String email;
}
