package com.qthegamep.pattern.project2.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;

public class HealthCheckerServletImpl extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(HealthCheckerServletImpl.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        int status = Response.Status.OK.getStatusCode();
        LOG.debug("Health checker status: {}", status);
        response.setStatus(status);
    }
}
