package com.example.webproject.tag;

import com.example.webproject.controller.command.Pagination;
import com.example.webproject.dao.entity.BaseEntity;
import jakarta.el.MethodExpression;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.JspWriter;
import jakarta.servlet.jsp.tagext.TagSupport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import static com.example.webproject.controller.PagePath.CONTROLLER;
import static com.example.webproject.controller.RequestAttribute.*;

public class PaginationTag extends TagSupport {
    private static final Logger logger = LogManager.getLogger(PaginationTag.class);

    private static final String ATTRIBUTE_AND = "&";
    private static final String ATTRIBUTES_START = "?";
    private static final String EQUALS = "=";

    private static final String PAGINATION_OPEN = "<ul class=\"pagination\">";
    private static final String PAGINATION_CLOSE = "</ul>";
    private static final String PAGE_ITEM_ENABLED = "<li class=\"page-item\">";
    private static final String PAGE_ITEM_DISABLED = " <li class=\"page-item disabled\">";
    private static final String PAGE_ITEM_CLOSE = "</li>";
    private static final String PAGE_LINK = "<a class=\"page-link\"";
    private static final String PAGE_LINK_CLOSE = "</a>";
    private static final String TAG_CLOSE = ">";
    private static final String HREF = " href=\"";
    private static final String PREVIOUS_KEY = "pagination.previous";
    private static final String NEXT_KEY = "pagination.next";

    private String command;

    public void setCommand(MethodExpression command) {
        this.command = command.getExpressionString();
    }


    @Override
    public int doStartTag() throws JspException {
        try {
            ServletRequest request = pageContext.getRequest();
            Pagination<? extends BaseEntity> pagination = (Pagination<? extends BaseEntity>) request.getAttribute(PAGINATION);
            JspWriter jspWriter = pageContext.getOut();
            jspWriter.write(PAGINATION_OPEN);
            ResourceBundle resourceBundle = TagUtil.findBundle(pageContext.getSession());
            String previousPlaceholder = resourceBundle.getString(PREVIOUS_KEY);
            String nextPlaceholder = resourceBundle.getString(NEXT_KEY);
            if (pagination.isPrevious()) {
                String previousHref = buildUrl(pagination, -1);
                String previousElement = buildElement(true, previousPlaceholder, previousHref);
                jspWriter.write(previousElement);
            } else {
                String previousElement = buildElement(false, previousPlaceholder, "");
                jspWriter.write(previousElement);
            }
            String currentElement = buildElement(false, String.valueOf(pagination.getCurrentPage() + 1), "");
            jspWriter.write(currentElement);
            if (pagination.isNext()) {
                String nextHref = buildUrl(pagination, 1);
                String nextElement = buildElement(true, nextPlaceholder, nextHref);
                jspWriter.write(nextElement);
            } else {
                String nextElement = buildElement(false, nextPlaceholder, "");
                jspWriter.write(nextElement);
            }
            jspWriter.write(PAGINATION_CLOSE);
            return EVAL_PAGE;
        } catch (IOException e) {
            logger.error("Can't display pagination panel:", e);
            throw new JspException("Can't display pagination panel", e);
        }
    }

    private String buildElement(boolean enabled, String placeholder, String href) {
        StringBuilder elementBuilder = new StringBuilder();
        if (enabled) {
            elementBuilder.append(PAGE_ITEM_ENABLED)
                    .append(PAGE_LINK)
                    .append(HREF)
                    .append(href)
                    .append("\"")
                    .append(TAG_CLOSE)
                    .append(placeholder)
                    .append(PAGE_LINK_CLOSE)
                    .append(PAGE_ITEM_CLOSE);
        } else {
            elementBuilder.append(PAGE_ITEM_DISABLED)
                    .append(PAGE_LINK)
                    .append(TAG_CLOSE)
                    .append(placeholder)
                    .append(PAGE_LINK_CLOSE)
                    .append(PAGE_ITEM_CLOSE);
        }
        return elementBuilder.toString();
    }

    private String buildUrl(Pagination<? extends BaseEntity> pagination, int addition) {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        StringBuilder href = new StringBuilder(request.getContextPath());
        href.append(CONTROLLER)
                .append(ATTRIBUTES_START)
                .append(COMMAND)
                .append(EQUALS)
                .append(command)
                .append(ATTRIBUTE_AND);
        Map<String, List<String>> requestAttributes = pagination.getRequestAttributes();
        for (Map.Entry<String, List<String>> entry : requestAttributes.entrySet()) {
            List<String> values = entry.getValue();
            for (String value : values) {
                href.append(entry.getKey());
                href.append(EQUALS);
                href.append(value);
                href.append(ATTRIBUTE_AND);
            }
        }
        href.append(PAGE);
        href.append(EQUALS);
        href.append(pagination.getCurrentPage() + addition);
        return href.toString();
    }
}
