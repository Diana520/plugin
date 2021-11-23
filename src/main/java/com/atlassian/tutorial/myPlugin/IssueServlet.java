package com.atlassian.tutorial.myPlugin;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.templaterenderer.TemplateRenderer;
import org.apache.velocity.context.Context;


import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.*;

@Scanned
public final class IssueServlet extends HttpServlet {
    @ComponentImport
    private final ActiveObjects ao;


    private IssueServiceTr issueService;
    private final TemplateRenderer renderer;

    @Inject
    public IssueServlet(ActiveObjects ao, IssueServiceTr issueService, TemplateRenderer renderer) {
        this.ao = checkNotNull(ao);
        this.issueService = issueService;
        this.renderer = renderer;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Map<String, Object> context = new HashMap<>();
        context.put("transitions", issueService.all());
        res.setContentType("text/html;charset=utf-8");
        renderer.render("admin.vm", context, res.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.getWriter().write("Todo servlet, doPost");
        res.getWriter().close();
    }
}
