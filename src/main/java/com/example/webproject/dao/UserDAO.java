package com.example.webproject.dao;

import com.example.webproject.dao.entity.User;
import com.example.webproject.exception.DAOException;

import java.util.Optional;

public interface UserDAO extends BaseDAO<User>{
    Optional<String> findEncryptedPassword(User user) throws DAOException;

    Optional<User> findUniqUser(User user) throws DAOException;

    Optional<User> findByEmail(String email) throws DAOException;

    Optional<User> findByPhone(String phone) throws DAOException;

    boolean updateUserPassword(User user, String hash) throws DAOException;
}
