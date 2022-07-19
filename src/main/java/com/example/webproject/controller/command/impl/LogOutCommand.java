package com.example.webproject.controller.command.impl;

import com.example.webproject.controller.command.Command;
import com.example.webproject.controller.command.Router;
import com.example.webproject.dao.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import static com.example.webproject.controller.PagePath.INDEX;
import static com.example.webproject.controller.SessionAttribute.USER;
import static com.example.webproject.controller.command.Router.DispatchType.REDIRECT;
import static com.example.webproject.dao.entity.User.UserRole.GUEST;

public class LogOutCommand implements Command {
    @Override
    public Router execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        User guest = User.getBuilder()
                .userRole(GUEST)
                .build();
        session.setAttribute(USER, guest);
        return new Router(REDIRECT, request.getContextPath() + INDEX);
    }
}
