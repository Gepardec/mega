package com.gepardec.mega;

import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.domain.model.monthlyreport.CommentDTO;
import com.gepardec.mega.service.api.comment.CommentService;
import io.quarkus.test.Mock;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
@Mock
public class CommentServiceMock implements CommentService {

    CommentService delegate;

    @Override
    public List<CommentDTO> findCommentsForEmployee(Employee employee) {
        return delegate.findCommentsForEmployee(employee);
    }
}
