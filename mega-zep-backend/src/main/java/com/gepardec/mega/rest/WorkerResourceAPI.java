package com.gepardec.mega.rest;

import com.gepardec.mega.application.interceptor.RolesAllowed;
import com.gepardec.mega.application.interceptor.Secured;
import com.gepardec.mega.db.entity.employee.User;
import com.gepardec.mega.domain.model.Role;
import com.gepardec.mega.domain.model.monthlyreport.MonthlyReport;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@RequestScoped
@Secured
@Path("/worker")
@RolesAllowed(Role.EMPLOYEE)
public interface WorkerResourceAPI {
    @GET
    @Path("/monthendreports")
    @Produces(MediaType.APPLICATION_JSON)
    MonthlyReport monthlyReport();

    @GET
    @Path("/getall")
    @Produces(MediaType.APPLICATION_JSON)
    List<User> getAll();
}
