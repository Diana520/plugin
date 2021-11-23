package com.atlassian.tutorial.myPlugin;


import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.checkNotNull;


@Path("/transition")
public class TransitionResource {
    private static final Logger log = LoggerFactory.getLogger(TransitionResource.class);

    private IssueServiceTr issueService;

    public TransitionResource(IssueServiceTr issueService) {
        log.info("transition resource created");
        this.issueService = issueService;
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") int id) {
        log.info("transition id to delete : " + id);
        issueService.delete(id);
    }
}
