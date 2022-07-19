package com.example.webproject.service;

import com.example.webproject.dao.entity.User;
import com.example.webproject.exception.ServiceException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService {
    Optional<User> findById(int id) throws ServiceException;

    Optional<User> authenticate(String email, char[] password) throws ServiceException;

    Optional<User> register(Map<String, String> credentialsMap) throws ServiceException;

    Optional<User> activate(User user) throws ServiceException;

    Optional<User> updateCredentials(Map<String, String> credentials) throws ServiceException;

    Optional<User> updateMyCredentials(Map<String, String> credentials) throws ServiceException;

    boolean updatePassword(User user, char[] newPassword) throws ServiceException;

    boolean findEmail(String email) throws ServiceException;

    boolean findPhone(String phone) throws ServiceException;
}
