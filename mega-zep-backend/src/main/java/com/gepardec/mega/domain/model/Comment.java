package com.gepardec.mega.domain.model;

import com.gepardec.mega.db.entity.employee.EmployeeState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    private Long id;

    private String message;

    private String authorEmail;

    private String authorName;

    private String updateDate;

    private EmployeeState state;
}
