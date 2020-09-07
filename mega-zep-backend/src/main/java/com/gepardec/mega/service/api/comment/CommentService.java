package com.gepardec.mega.service.api.comment;

import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.domain.model.monthlyreport.CommentDTO;

import java.util.List;

public interface CommentService {
    List<CommentDTO> findCommentsForEmployee(Employee employee);
}
