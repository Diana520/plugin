package com.atlassian.tutorial.myPlugin;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Date;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Lists.newArrayList;

@Scanned
@Named
public class IssueServiceImpl implements IssueServiceTr {
    @ComponentImport
    private final ActiveObjects ao;
    private static final Logger log = LoggerFactory.getLogger(TransitionResource.class);

    @Inject
    public IssueServiceImpl(ActiveObjects ao) {
        this.ao = checkNotNull(ao);
    }

    @Override
    public IssuePr add(String to, String from, Date date, String key) {
        final IssuePr issue1 = ao.create(IssuePr.class);
        issue1.setIssueKey(key);
        issue1.setDate(date);
        issue1.setFrom(from);
        issue1.setTo(to);
        issue1.save();
        return issue1;
    }

    @Override
    public List<IssuePr> all() {
        return newArrayList(ao.find(IssuePr.class));
    }

    @Override
    public void delete(int id) {
        IssuePr issuePr = ao.get(IssuePr.class, id);
        log.info("get by id: "+issuePr);
       ao.delete(ao.get(IssuePr.class, id));
    }
}
