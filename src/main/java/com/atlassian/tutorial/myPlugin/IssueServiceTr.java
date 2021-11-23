package com.atlassian.tutorial.myPlugin;

import com.atlassian.activeobjects.tx.Transactional;

import java.util.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Transactional
public interface IssueServiceTr {
    IssuePr add(String to, String from, Date date, String key);

    List<IssuePr> all();

    void delete(int id);
}
