package com.example.webproject.controller;

import com.example.webproject.controller.command.Command;
import com.example.webproject.controller.command.CommandProvider;
import com.example.webproject.controller.command.Router;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Optional;

import static com.example.webproject.controller.PagePath.ERROR_500;
import static com.example.webproject.controller.RequestAttribute.COMMAND;
import static com.example.webproject.controller.RequestAttribute.EXCEPTION_MESSAGE;
import static com.example.webproject.controller.command.Router.DispatchType.FORWARD;

@WebServlet(name = "controller", urlPatterns = {"/controller"})
public class Controller extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(Controller.class);

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }


    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String commandName = request.getParameter(COMMAND);
        CommandProvider commandProvider = CommandProvider.getInstance();
        Optional<Command> commandOptional = commandProvider.defineCommand(commandName);
        Router router;
        if (commandOptional.isPresent()) {
            Command command = commandOptional.get();
            router = command.execute(request, response);
        } else {
            request.setAttribute(EXCEPTION_MESSAGE, "Command is not presented");
            router = new Router(FORWARD, ERROR_500);
        }
        switch (router.getDispatchType()) {
            case FORWARD:
                RequestDispatcher dispatcher = request.getRequestDispatcher(router.getTargetPath());
                dispatcher.forward(request, response);
                break;
            case REDIRECT:
                response.sendRedirect(router.getTargetPath());
                break;
            default:
                logger.error("Invalid router type!");
                response.sendError(500);
        }
    }
}
