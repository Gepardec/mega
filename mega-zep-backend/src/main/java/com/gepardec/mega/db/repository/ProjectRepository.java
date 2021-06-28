package com.gepardec.mega.db.repository;

import com.gepardec.mega.db.entity.project.Project;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@ApplicationScoped
@Transactional
public class ProjectRepository implements PanacheRepository<Project> {

    @Inject
    EntityManager em;

    public Project findByName(String name){
        return find("name", name).firstResult();
    }

    public Project merge(Project project) {
        return em.merge(project);
    }
}
