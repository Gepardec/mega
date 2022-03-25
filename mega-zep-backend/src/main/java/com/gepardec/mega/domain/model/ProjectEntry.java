package com.gepardec.mega.domain.model;

import com.gepardec.mega.db.entity.project.ProjectStep;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectEntry {
    private LocalDate date;

    private String name;

    private ProjectStep step;

    private User assignee;

    private User owner;

    private Project project;
}
