package com.gepardec.mega.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

// Represents the logged user in mega.

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private long dbId;

    private String userId;

    private String email;

    private String firstname;

    private String lastname;

    private LocalDate releaseDate;

    private Set<Role> roles;
}
