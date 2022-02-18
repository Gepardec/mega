package com.gepardec.mega.rest.api;

import com.gepardec.mega.db.entity.employee.User;
import com.gepardec.mega.domain.model.monthlyreport.MonthlyReport;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/worker")
public interface WorkerResource {
    @GET
    @Path("/monthendreports")
    @Produces(MediaType.APPLICATION_JSON)
    MonthlyReport monthlyReport();

    @GET
    @Path("/getall")
    @Produces(MediaType.APPLICATION_JSON)
    List<User> getAll();

    @GET
    @Path("/monthendreports/{year}/{month}")
    @Produces(MediaType.APPLICATION_JSON)
    MonthlyReport monthlyReport(@PathParam("year") Integer year, @PathParam("month") Integer month);
}
