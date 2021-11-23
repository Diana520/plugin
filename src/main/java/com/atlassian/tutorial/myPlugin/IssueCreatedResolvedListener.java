package com.atlassian.tutorial.myPlugin;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.event.api.EventListener;
import com.atlassian.event.api.EventPublisher;
import com.atlassian.jira.event.issue.IssueChangedEvent;
import com.atlassian.jira.event.issue.IssueEvent;
import com.atlassian.jira.event.type.EventType;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.history.ChangeItemBean;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.plugin.spring.scanner.annotation.imports.JiraImport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class IssueCreatedResolvedListener implements InitializingBean, DisposableBean {
    private static final Logger log = LoggerFactory.getLogger(IssueCreatedResolvedListener.class);
    private ExecutorService service = Executors.newSingleThreadExecutor();

    @JiraImport
    private final EventPublisher eventPublisher;

    private String keyIssue;


    private IssueServiceTr issueService;


    @Autowired
    public IssueCreatedResolvedListener(EventPublisher eventPublisher, IssueServiceTr issueService) {
        this.eventPublisher = eventPublisher;
        this.issueService = issueService;
        log.error("IssueCreatedResolvedListener created");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("Enabling plugin");
        eventPublisher.register(this);
    }

    /**
     * Called when the plugin is being disabled or removed.
     *
     * @throws Exception
     */
    @Override
    public void destroy() throws Exception {
        log.info("Disabling plugin");
        eventPublisher.unregister(this);
    }

    @EventListener
    public void onIssueEvent(IssueEvent issueEvent) throws ExecutionException, InterruptedException {
        Long eventTypeId = issueEvent.getEventTypeId();
        Issue issue = issueEvent.getIssue();
        keyIssue = issue.getKey();
        Runnable r = () -> {
            log.info(" issue changed to: " + issue.getStatus().getName().toString());
            if (eventTypeId.equals(EventType.ISSUE_CREATED_ID)) {
                log.info("Issue {} has been created at {}.", issue.getKey(), issue.getCreated());
            } else if (eventTypeId.equals(EventType.ISSUE_RESOLVED_ID)) {
                log.info("Issue {} has been resolved at {}.", issue.getKey(), issue.getResolutionDate());
            } else if (eventTypeId.equals(EventType.ISSUE_CLOSED_ID)) {
                log.info("Issue {} has been closed at {}.", issue.getKey(), issue.getUpdated());
            }
        };
        service.submit(r);
    }

    @EventListener
    public void onIssueChangeEvent(IssueChangedEvent issueChangedEvent) {
        ChangeItemBean bean = issueChangedEvent.getChangeItems().iterator().next();
        log.info("from status: " + bean.getFromString() + " to: " + bean.getToString());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date();
        issueService.add(bean.getToString(), bean.getFromString(), date, keyIssue);
    }
}
