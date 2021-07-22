package com.gepardec.mega.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDate;


@JsonIgnoreProperties(ignoreUnknown = true)
@Jacksonized
@Builder
@Getter
public class ProjectCommentDto {

    private Long id;
    private String comment;
    private LocalDate date;
    private String projectName;

}
