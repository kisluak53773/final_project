package com.example.webproject.controller.command.impl;

import com.example.webproject.controller.command.Command;
import com.example.webproject.controller.command.Router;
import com.example.webproject.dao.entity.User;
import com.example.webproject.exception.ServiceException;
import com.example.webproject.service.impl.UserServiceImpl;
import com.example.webproject.service.util.RequestHandler;
import com.example.webproject.service.validator.impl.UserValidatorImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Optional;

import static com.example.webproject.controller.PagePath.*;
import static com.example.webproject.controller.RequestAttribute.*;
import static com.example.webproject.controller.SessionAttribute.USER;
import static com.example.webproject.controller.command.Router.DispatchType.FORWARD;
import static com.example.webproject.controller.command.Router.DispatchType.REDIRECT;
import static com.example.webproject.service.validator.impl.UserValidatorImpl.NOT_VALID;

public class RegisterCommand implements Command {
    private static final Logger logger = LogManager.getLogger(RegisterCommand.class);

    @Override
    public Router execute(HttpServletRequest request, HttpServletResponse response) {
        RequestHandler requestHandler = RequestHandler.getInstance();
        Map<String, String> userData = requestHandler.findUserData(request.getParameterMap());
        UserValidatorImpl userValidator = UserValidatorImpl.getInstance();
        Map<String, String> validatedUser = userValidator.validateUser(userData);
        if (!validatedUser.containsValue(NOT_VALID)) {
            try {
                checkPresence(validatedUser);
                if (!validatedUser.containsValue(NOT_VALID)) {
                    UserServiceImpl userService = UserServiceImpl.getInstance();
                    Optional<User> registeredUser = userService.register(userData);
                    User user = registeredUser.orElseThrow(ServiceException::new);
                    HttpSession session = request.getSession();
                    session.setAttribute(USER, user);
                    logger.debug("Registered new User[id=" + user.getId() + "]");
                    return new Router(REDIRECT, request.getContextPath());
                }
            } catch (ServiceException e) {
                logger.error("Can't validate registration credential", e);
                request.setAttribute(EXCEPTION_MESSAGE, "Can't validate registration credential: " + e.getMessage());
                return new Router(REDIRECT, request.getContextPath() + ERROR_500);
            }
        }
        return new Router(FORWARD, LOGIN_PAGE);
    }

    private void checkPresence(Map<String, String> credentialsMap) throws ServiceException {
        UserServiceImpl userService = UserServiceImpl.getInstance();
        String email = credentialsMap.get(EMAIL);
        String phone = credentialsMap.get(PHONE);
        if (userService.findEmail(email)) {
            credentialsMap.put(EMAIL, NOT_VALID);
        }
        if (userService.findPhone(phone)) {
            credentialsMap.put(PHONE, NOT_VALID);
        }
    }
}
