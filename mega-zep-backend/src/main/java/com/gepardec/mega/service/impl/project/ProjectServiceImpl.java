package com.gepardec.mega.service.impl.project;

import com.gepardec.mega.domain.model.User;
import com.gepardec.mega.service.api.project.ProjectService;
import com.gepardec.mega.zep.ZepService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class ProjectServiceImpl implements ProjectService {

    @Inject
    ZepService zepService;


    @Override
    public Map<User, List<String>> getProjectsForUsersAndYear(final LocalDate monthYear, final List<User> users) {
        return zepService.getProjectsForUsersAndYear(monthYear, users);
    }

    public Map<String, List<User>> getProjectLeadsForProjectsAndYear(final LocalDate monthYear, final List<User> users) {
        return zepService.getProjectLeadsForProjectsAndYear(monthYear, users);
    }
}
