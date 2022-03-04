package com.gepardec.mega.rest;

import com.gepardec.mega.domain.model.monthlyreport.MonthlyReport;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/worker")
public interface WorkerResourceAPI {
    @GET
    @Path("/monthendreports")
    @Produces(MediaType.APPLICATION_JSON)
    MonthlyReport monthlyReport();

    @GET
    @Path("/monthendreports/{year}/{month}")
    @Produces(MediaType.APPLICATION_JSON)
    MonthlyReport monthlyReport(@PathParam("year") Integer year, @PathParam("month") Integer month);
}