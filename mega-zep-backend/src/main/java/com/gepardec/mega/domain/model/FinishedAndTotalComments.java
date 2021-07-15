package com.gepardec.mega.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.auto.value.AutoValue;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.jackson.Jacksonized;

@Builder
@Getter
@ToString
@EqualsAndHashCode
@Accessors(fluent = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Jacksonized
public class FinishedAndTotalComments {


    private final Long finishedComments;

    private final Long totalComments;


}
