package com.gepardec.mega.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.enterprise.inject.Vetoed;

@Vetoed
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserContext {
    private User user;
}
