package com.atlassian.tutorial.myPlugin.webwork;

import com.atlassian.jira.bc.issue.IssueService;
import com.atlassian.jira.config.StatusManager;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.IssueInputParameters;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.status.Status;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import com.atlassian.jira.workflow.JiraWorkflow;
import com.atlassian.jira.workflow.WorkflowManager;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.opensymphony.workflow.loader.ActionDescriptor;
import com.opensymphony.workflow.loader.StepDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Collection;
import java.util.List;

public class KitchenDutyPlanningWebworkAction extends JiraWebActionSupport {
    private static final Logger log = LoggerFactory.getLogger(KitchenDutyPlanningWebworkAction.class);
    private final StatusManager statusManager;
    private Collection<Status> statuses;
    private String issueKey;
    private final IssueManager issueManager;
    private String status;
    private final WorkflowManager workflowManager;
    private final JiraAuthenticationContext jiraApplicationContext;
    private final IssueService issueService;


    @Inject
    public KitchenDutyPlanningWebworkAction(@ComponentImport StatusManager statusManager,
                                            @ComponentImport IssueManager issueManager,
                                            @ComponentImport WorkflowManager workflowManager,
                                            @ComponentImport JiraAuthenticationContext jiraApplicationContext,
                                            @ComponentImport IssueService issueService) {
        this.statusManager = statusManager;
        this.issueManager = issueManager;
        this.workflowManager = workflowManager;
        this.jiraApplicationContext = jiraApplicationContext;
        this.issueService = issueService;
    }

    public Collection<Status> getStatuses() {
        return statuses;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setIssueKey(String issueKey) {
        this.issueKey = issueKey;
    }

    @Override
    public String execute() throws Exception {
        statuses = statusManager.getStatuses();
        Issue issue = issueManager.getIssueObject(issueKey);
        if (issue != null) {
            Status currentStatus = issue.getStatus();
            Status resolvedStatus = statusManager.getStatus(status);
            if (!currentStatus.equals(resolvedStatus)) {
                JiraWorkflow workflow = workflowManager.getWorkflow(issue);
                StepDescriptor resolvedStatusStep = workflow.getLinkedStep(resolvedStatus);
                if (resolvedStatusStep != null) {
                    Collection<ActionDescriptor> resolveActions = workflow.getActionsWithResult(resolvedStatusStep);
                    StepDescriptor currentStatusStep = workflow.getLinkedStep(currentStatus);
                    List currentActions = currentStatusStep.getActions();
                    resolveActions.retainAll(currentActions);
                    ActionDescriptor resolveAction = resolveActions.iterator().next();
                    ApplicationUser currentUser = jiraApplicationContext.getLoggedInUser();
                    Long issueId = issue.getId();
                    int resolveActionId = resolveAction.getId();
                    IssueInputParameters issueInputParameters = issueService.newIssueInputParameters();
                    IssueService.TransitionValidationResult transitionValidationResult = issueService.validateTransition(currentUser,
                            issueId, resolveActionId, issueInputParameters);
                    issueService.transition(currentUser, transitionValidationResult);
                }else{
                    log.info("it's not available transition to this status");
                }
            }
        }
        return "kitchen-duty-planning-success";
    }
}
