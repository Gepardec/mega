package com.gepardec.mega.service.impl.stepentry;

import com.gepardec.mega.db.entity.State;
import com.gepardec.mega.domain.model.Employee;
import com.gepardec.mega.domain.utils.DateUtils;
import com.gepardec.mega.service.api.stepentry.StepEntryService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.LocalDate;

@ApplicationScoped
public class StepEntryServiceImpl implements StepEntryService {

    @Inject
    EntityManager entityManager;

    public State getEmcState(Employee employee) {
        LocalDate fromDate = LocalDate.parse(DateUtils.getFirstDayOfFollowingMonth(employee.releaseDate()));
        LocalDate toDate = LocalDate.parse(DateUtils.getLastDayOfFollowingMonth(employee.releaseDate()));

        // TODO: Use PanacheRepository instead of EntityManager. Problem: Using .project(State.class) is not working with Panache.
        State state = entityManager.createQuery("SELECT s.state " +
                "FROM StepEntry s " +
                "WHERE s.owner.email = :email AND s.assignee.email = :email AND s.date BETWEEN :fromDate AND :toDate", State.class)
                .setParameter("email", employee.email())
                .setParameter("fromDate", fromDate)
                .setParameter("toDate", toDate)
                .getSingleResult();

        return state;
    }

}
