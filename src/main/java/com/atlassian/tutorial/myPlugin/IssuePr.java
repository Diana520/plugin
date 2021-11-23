package com.atlassian.tutorial.myPlugin;

import net.java.ao.Entity;

import java.util.Date;

public interface IssuePr extends Entity {
    String getIssueKey();


    void setIssueKey(String issueKey);

    String getFrom();

    void setFrom(String from);

    String getTo();

    void setTo(String to);

    Date getDate();

    void setDate(Date date);


}
