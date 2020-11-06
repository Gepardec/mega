package com.gepardec.mega.service.impl.project;

import com.gepardec.mega.domain.model.Project;
import com.gepardec.mega.service.api.project.ProjectService;
import com.gepardec.mega.zep.ZepService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class ProjectServiceImpl implements ProjectService {

    @Inject
    ZepService zepService;


    @Override
    public List<Project> getProjectsForYear(final LocalDate monthYear) {
        return zepService.getProjectsForYear(monthYear);
    }
}
