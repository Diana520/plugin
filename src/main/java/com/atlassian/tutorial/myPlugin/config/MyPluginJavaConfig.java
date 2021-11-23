package com.atlassian.tutorial.myPlugin.config;


import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.event.api.EventPublisher;
import com.atlassian.jira.bc.issue.IssueService;
import com.atlassian.jira.config.StatusManager;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.workflow.WorkflowManager;
import com.atlassian.templaterenderer.TemplateRenderer;
import com.atlassian.tutorial.myPlugin.IssueCreatedResolvedListener;
import com.atlassian.tutorial.myPlugin.IssueServiceImpl;
import com.atlassian.tutorial.myPlugin.IssueServiceTr;
import com.atlassian.tutorial.myPlugin.api.MyPluginComponent;
import com.atlassian.tutorial.myPlugin.impl.MyPluginComponentImpl;
import com.atlassian.plugins.osgi.javaconfig.configs.beans.ModuleFactoryBean;
import com.atlassian.plugins.osgi.javaconfig.configs.beans.PluginAccessorBean;
import com.atlassian.sal.api.ApplicationProperties;
import org.osgi.framework.ServiceRegistration;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static com.atlassian.plugins.osgi.javaconfig.OsgiServices.exportOsgiService;
import static com.atlassian.plugins.osgi.javaconfig.OsgiServices.importOsgiService;

@Configuration
@Import({
        ModuleFactoryBean.class,
        PluginAccessorBean.class
})
public class MyPluginJavaConfig {


    // imports ApplicationProperties from OSGi
    @Bean
    public ApplicationProperties applicationProperties() {
        return importOsgiService(ApplicationProperties.class);
    }

    @Bean
    public MyPluginComponent myPluginComponent(ApplicationProperties applicationProperties) {
        return new MyPluginComponentImpl(applicationProperties);
    }

    @Bean
    public StatusManager statusManager() {
        return importOsgiService(StatusManager.class);
    }

    // Exports MyPluginComponent as an OSGi service
    @Bean
    public FactoryBean<ServiceRegistration> registerMyDelegatingService(
            final MyPluginComponent mypluginComponent) {
        return exportOsgiService(mypluginComponent, null, MyPluginComponent.class);
    }

    @Bean
    public IssueManager issueManager() {
        return importOsgiService(IssueManager.class);
    }

    @Bean
    public WorkflowManager workflowManager() {
        return importOsgiService(WorkflowManager.class);
    }

    @Bean
    public JiraAuthenticationContext jiraAuthenticationContext() {
        return importOsgiService(JiraAuthenticationContext.class);
    }

    @Bean
    public IssueService issueService() {
        return importOsgiService(IssueService.class);
    }

    @Bean
    public IssueServiceTr issueServiceTr(ActiveObjects activeObjects) {
        return new IssueServiceImpl(activeObjects);
    }

    @Bean
    public EventPublisher eventPublisher() {
        return importOsgiService(EventPublisher.class);
    }

    @Bean
    public IssueCreatedResolvedListener listener(EventPublisher eventPublisher, IssueServiceTr issueService) {
        return new IssueCreatedResolvedListener(eventPublisher, issueService);
    }

    @Bean
    public TemplateRenderer templateRenderer() {
        return importOsgiService(TemplateRenderer.class);
    }

    @Bean
    public ActiveObjects activeObjects() {
        return importOsgiService(ActiveObjects.class);
    }


}