package com.gepardec.mega.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    private String userId;

    private String email;

    private String title;

    private String firstname;

    private String lastname;

    private String salutation;

    private String releaseDate;

    private String workDescription;

    private String language;

    private boolean active;
}
