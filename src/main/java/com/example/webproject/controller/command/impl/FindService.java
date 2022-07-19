package com.example.webproject.controller.command.impl;

import com.example.webproject.controller.command.Command;
import com.example.webproject.controller.command.Pagination;
import com.example.webproject.controller.command.Router;
import com.example.webproject.dao.entity.Service;
import com.example.webproject.exception.ServiceException;
import com.example.webproject.service.ServiceService;
import com.example.webproject.service.impl.ServiceServiceImpl;
import com.example.webproject.service.util.RequestHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;

import static com.example.webproject.controller.PagePath.ERROR_500;
import static com.example.webproject.controller.PagePath.SERVICES_PAGE;
import static com.example.webproject.controller.RequestAttribute.EXCEPTION_MESSAGE;
import static com.example.webproject.controller.RequestAttribute.PAGINATION;
import static com.example.webproject.controller.command.Router.DispatchType.FORWARD;

public class FindService implements Command {
    private static final Logger logger = LogManager.getLogger(FindService.class);

    @Override
    public Router execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            ServiceService serviceService = ServiceServiceImpl.getInstance();
            RequestHandler parameterConverter = RequestHandler.getInstance();
            Map<String, List<String>> parameterMap = parameterConverter.transform(request.getParameterMap());
            Pagination<Service> pagination = serviceService.findServices(parameterMap);
            request.setAttribute(PAGINATION, pagination);
            return new Router(FORWARD, SERVICES_PAGE);
        } catch (ServiceException e) {
            logger.error("Error occurred while loading paginated data", e);
            request.setAttribute(EXCEPTION_MESSAGE, "Error occurred while loading paginated data: " + e.getMessage());
            return new Router(FORWARD, ERROR_500);
        }
    }
}
