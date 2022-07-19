package com.example.webproject.controller.command.impl;

import com.example.webproject.controller.command.Command;
import com.example.webproject.controller.command.Router;
import com.example.webproject.dao.entity.User;
import com.example.webproject.exception.ServiceException;
import com.example.webproject.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.util.Optional;

import static com.example.webproject.controller.PagePath.ERROR_500;
import static com.example.webproject.controller.PagePath.LOGIN_PAGE;
import static com.example.webproject.controller.RequestAttribute.*;
import static com.example.webproject.controller.SessionAttribute.USER;
import static com.example.webproject.controller.command.Router.DispatchType.FORWARD;
import static com.example.webproject.controller.command.Router.DispatchType.REDIRECT;

public class LoginCommand implements Command {
    @Override
    public Router execute(HttpServletRequest request, HttpServletResponse response) {
        Router router;
        String email = request.getParameter(EMAIL);
        String password = request.getParameter(PASSWORD);
        try {
            UserServiceImpl service = UserServiceImpl.getInstance();
            Optional<User> optionalUser = service.authenticate(email, password.toCharArray());
            if (optionalUser.isPresent()) {
                HttpSession session = request.getSession();
                User user = optionalUser.get();
                session.setAttribute(USER, user);
                router = new Router(REDIRECT, request.getContextPath());
            } else {
                router = new Router(FORWARD, LOGIN_PAGE);
            }
        } catch (ServiceException e) {
            request.setAttribute(EXCEPTION_MESSAGE, e.getMessage());
            router = new Router(FORWARD, ERROR_500);
        }
        return router;
    }
}
