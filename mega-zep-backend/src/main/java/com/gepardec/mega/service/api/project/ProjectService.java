package com.gepardec.mega.service.api.project;

import com.gepardec.mega.domain.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ProjectService {

    Map<User, List<String>> getProjectsForUsersAndYear(final LocalDate monthYear, final List<User> users);

    Map<String, List<User>> getProjectLeadsForProjectsAndYear(LocalDate monthYear, List<User> users);
}
