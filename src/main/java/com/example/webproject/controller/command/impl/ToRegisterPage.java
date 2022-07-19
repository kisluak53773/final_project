package com.example.webproject.controller.command.impl;

import com.example.webproject.controller.command.Command;
import com.example.webproject.controller.command.Router;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static com.example.webproject.controller.PagePath.REGISTER_PAGE;
import static com.example.webproject.controller.command.Router.DispatchType.FORWARD;

public class ToRegisterPage implements Command {
    @Override
    public Router execute(HttpServletRequest request, HttpServletResponse response) {
        return new Router(FORWARD,REGISTER_PAGE);
    }
}
