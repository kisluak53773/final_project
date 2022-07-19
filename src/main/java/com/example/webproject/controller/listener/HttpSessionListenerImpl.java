package com.example.webproject.controller.listener;

import com.example.webproject.dao.entity.User;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

import static com.example.webproject.controller.RequestAttribute.LOCALE;
import static com.example.webproject.controller.SessionAttribute.USER;
import static com.example.webproject.dao.entity.User.UserRole.GUEST;

@WebListener
public class HttpSessionListenerImpl implements HttpSessionListener{
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        User guest = User.getBuilder()
                .userRole(GUEST)
                .build();
        session.setAttribute(USER, guest);
        session.setAttribute(LOCALE, "en_US");
    }
}
