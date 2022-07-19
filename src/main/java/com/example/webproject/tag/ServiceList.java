package com.example.webproject.tag;

import com.example.webproject.controller.command.Pagination;
import com.example.webproject.dao.entity.Service;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.JspWriter;
import jakarta.servlet.jsp.tagext.TagSupport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;

import static com.example.webproject.controller.RequestAttribute.PAGINATION;

public class ServiceList extends TagSupport {
    private static final Logger logger = LogManager.getLogger(ServiceList.class);

    private static final String NOTHING_FOUND = "search.nothingFound";
    private static final String FREE_ANNOUNCEMENT = "announcements.free";
    private static final String MY_ANNOUNCEMENT = "announcements.myAnnouncement";

    @Override
    public int doStartTag() throws JspException {
        try {
            ServletRequest request = pageContext.getRequest();
            ResourceBundle resourceBundle = TagUtil.findBundle(pageContext.getSession());
            Pagination<Service> pagination = (Pagination<Service>) request.getAttribute(PAGINATION);
            List<Service> services = pagination.getData();
            JspWriter jspWriter = pageContext.getOut();

            if (services.isEmpty()) {
                jspWriter.write("<div class=\"card\">" +
                        resourceBundle.getString(NOTHING_FOUND) +
                        "</div>");
            }
            for (Service service : services) {
                String row = buildAnnouncementRow(service);
                jspWriter.write(row);
            }
        } catch (IOException e) {
            logger.error("Can't display announcements table:", e);
            throw new JspException("Can't display announcements table", e);
        }
        return EVAL_PAGE;
    }

    private String buildAnnouncementRow(Service service) {
        HttpSession session = pageContext.getSession();
        ResourceBundle resourceBundle = TagUtil.findBundle(session);
        float price = service.getPrice().floatValue();
        String priceTag = (price + "BYN");
        StringBuilder builder = new StringBuilder()
                .append("<div id=\"")
                .append(service.getId())
                .append("\" class=\"card adCard\">")
                .append("<div class=\"row\">")
                .append("<div class=\"adCard-img\">")
                .append("<img src=\"")
                .append(service.getServiceImage())
                .append("\" width=\"130\" alt=\"No image\"/>")
                .append("</div>")
                .append("<div class=\"col-md-7\">")
                .append("<div class=\"card-body\">")
                .append("<h5 class=\"card-title\">")
                .append(service.getServiceName())
                .append("</h5>")
                .append(service.getServiceDescription())
                .append("</div>")
                .append("</div>")
                .append("<div class=\"col-md-3\">")
                .append("<div class=\"card-price\">")
                .append("<h3>")
                .append(priceTag)
                .append("</h3>")
                .append("</div>")
                .append("</div>")
                .append("</div>")
                .append("</div>");
        return builder.toString();
    }
}
