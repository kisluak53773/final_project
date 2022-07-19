package com.example.webproject.service.impl;

import com.example.webproject.dao.entity.User;
import com.example.webproject.dao.impl.DAOAllocator;
import com.example.webproject.dao.impl.UserDAOImpl;
import com.example.webproject.exception.DAOException;
import com.example.webproject.exception.ServiceException;
import com.example.webproject.service.UserService;
import com.example.webproject.service.util.PasswordCryptor;
import com.example.webproject.service.validator.impl.UserValidatorImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import static com.example.webproject.controller.RequestAttribute.*;
import static com.example.webproject.dao.entity.User.UserRole.CUSTOMER;
import static com.example.webproject.service.validator.impl.UserValidatorImpl.NOT_VALID;

public class UserServiceImpl implements UserService {
    private static final UserServiceImpl instance = new UserServiceImpl();
    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

    private static final int USERS_ON_PAGE = 15;

    private UserServiceImpl() {
    }

    public static UserServiceImpl getInstance() {
        return instance;
    }

    @Override
    public Optional<User> findById(int id) throws ServiceException {
        return Optional.empty();// TODO: 11.04.2022
    }

    @Override
    public Optional<User> authenticate(String email, char[] password) throws ServiceException {
        UserValidatorImpl validator = UserValidatorImpl.getInstance();
        User user;
        if (validator.validateEmail(email)) {
            user = User.getBuilder()
                    .email(email)
                    .build();
        } else {
            logger.debug("Can't recognize valid auth field!");
            return Optional.empty();
        }

        if (!validator.validatePassword(String.valueOf(password))) {
            return Optional.empty();
        }

        UserDAOImpl userDao = DAOAllocator.getUserDao();
        try {
            Optional<String> optionalHash = userDao.findEncryptedPassword(user);
            if (optionalHash.isPresent()) {
                String hash = optionalHash.get();
                PasswordCryptor cryptor = PasswordCryptor.getInstance();
                boolean verified = cryptor.verify(password, hash.toCharArray());

                if (verified) {
                    Optional<User> optionalUser = userDao.findUniqUser(user);
                    User authenticatedUser = optionalUser.orElseThrow(DAOException::new);
                    return Optional.of(authenticatedUser);
                }
            }
            return Optional.empty();
        } catch (DAOException e) {
            logger.error("Can't authenticate user", e);
            throw new ServiceException("Can't authenticate user", e);
        }
    }

    @Override
    public Optional<User> register(Map<String, String> credentialsMap) throws ServiceException {
        UserValidatorImpl validator = UserValidatorImpl.getInstance();
        Map<String, String> validatedCredentials = validator.validateUser(credentialsMap);
        if (validatedCredentials.containsValue(NOT_VALID)) {
            return Optional.empty();
        }
        User user = User.getBuilder()
                .email(validatedCredentials.get(EMAIL))
                .phone(validatedCredentials.get(PHONE))
                .userRole(CUSTOMER)
                .isDeleted(false)
                .build();
        UserDAOImpl userDao = DAOAllocator.getUserDao();
        try {
            Optional<User> optionalSaved = userDao.save(user);
            if (!optionalSaved.isPresent()) {
                logger.error("Saved user is empty!");
                throw new ServiceException("UserDao returned empty saved user!");
            }
            User savedUser = optionalSaved.get();
            char[] password = validatedCredentials.get(PASSWORD).toCharArray();
            updatePassword(savedUser, password);
            return Optional.of(savedUser);

        } catch (DAOException e) {
            logger.error("Can't register user", e);
            throw new ServiceException("Can't register user", e);
        }
    }

    @Override
    public Optional<User> activate(User user) throws ServiceException {
        return Optional.empty();
    }

    @Override
    public Optional<User> updateCredentials(Map<String, String> credentials) throws ServiceException {
        return Optional.empty();
    }

    @Override
    public Optional<User> updateMyCredentials(Map<String, String> credentials) throws ServiceException {
        return Optional.empty();
    }

    @Override
    public boolean updatePassword(User user, char[] newPassword) throws ServiceException {
        try {
            UserDAOImpl userDao = DAOAllocator.getUserDao();
            PasswordCryptor cryptor = PasswordCryptor.getInstance();
            String encrypted = cryptor.encrypt(newPassword);
            return userDao.updateUserPassword(user, encrypted);
        } catch (DAOException e) {
            logger.error("Can't update user id= " + user.getId() + " password: ", e);
            throw new ServiceException("Can't update user password: ", e);
        }
    }

    @Override
    public boolean findEmail(String email) throws ServiceException {
        return false;
    }

    @Override
    public boolean findPhone(String phone) throws ServiceException {
        return false;
    }
}
