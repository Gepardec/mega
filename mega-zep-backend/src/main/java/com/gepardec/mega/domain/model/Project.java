package com.gepardec.mega.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Project {
    private String projectId;

    private String description;

    private LocalDate startDate;

    private LocalDate endDate;

    private List<String> employees;

    private List<String> leads;

    private List<String> categories;
}