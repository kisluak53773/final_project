package com.example.webproject.dao.impl;

import com.example.webproject.connectionpool.CustomConnectionPool;
import com.example.webproject.dao.UserDAO;
import com.example.webproject.dao.entity.User;
import com.example.webproject.dao.mapper.impl.UserMapper;
import com.example.webproject.exception.DAOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.webproject.dao.ColumnName.*;
import static com.example.webproject.dao.TableName.USERS;

public class UserDAOImpl implements UserDAO {
    private static final String SELECT_USERS="SELECT "+ID_USER+", "+EMAIL+ ", "+PASSWORD+ ", "+USER_ROLE+ ", "+
            IS_DELETED+ ", "+PHONE+" FROM "+USERS+";";
    private static final String SELECT_USER_BY_ID="SELECT "+ID_USER+", "+EMAIL+ ", "+PASSWORD+ ", "+USER_ROLE+ ", "+
            IS_DELETED+ ", "+PHONE+" FROM "+USERS+
            " WHERE "+ID_USER+" = " + "?" + ";";
    private static final String SELECT_USER_ID_BY_EMAIL="SELECT "+ID_USER+" FROM "+USERS+" WHERE "+EMAIL+"="+"?"+";";
    private static final String SELECT_USER_ID_BY_PHONE = "SELECT " + ID_USER + " FROM " + USERS
            + " WHERE " + PHONE + "=?;";
    private static final String UPDATE_USER = "UPDATE " + USERS + " SET "+EMAIL+ "=?, "+USER_ROLE+ "=?, "+
            IS_DELETED+ "=?, "+PHONE+ "=?, "+ " WHERE " + ID_USER + "=?;";
    private static final String FIND_USER_PASSWORD="SELECT " + PASSWORD + " FROM " + USERS
            + " WHERE " + ID_USER + "=?;";
    private static final String INSERT_NEW_USER = "INSERT INTO " + USERS + " ("+EMAIL+ ", "+PASSWORD+ ", "+USER_ROLE+
            ", "+ IS_DELETED+ ", "+PHONE+") " + "VALUES(?,?,?,?,?);";
    private static final String UPDATE_USER_PASSWORD = "UPDATE " + USERS + " SET " + PASSWORD + "=?"+
            " WHERE " + ID_USER+ "="+"?"+";";
    private static final String UNDEFINED_USER_PASSWORD = "UNDEFINED";

    private static final Logger logger = LogManager.getLogger(UserDAOImpl.class);
    private final CustomConnectionPool pool = CustomConnectionPool.getInstance();

    @Override
    public List<User> findAll() throws DAOException {
        try (
                Connection connection = pool.getConnection();
                PreparedStatement statement = connection.prepareStatement(SELECT_USERS);
                ResultSet resultSet = statement.executeQuery()
        ) {

            List<User> users = new ArrayList<>();
            UserMapper userMapper = UserMapper.getInstance();
            while (resultSet.next()) {
                User user = userMapper.map(resultSet);
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            logger.error("Error occurred while loading all users", e);
            throw new DAOException("Error occurred while loading users", e);
        }
    }

    @Override
    public Optional<User> findById(int id) throws DAOException {
        try (
                Connection connection = pool.getConnection();
                PreparedStatement statement = connection.prepareStatement(SELECT_USER_BY_ID)
        ) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.next()) {
                return Optional.empty();
            }
            UserMapper userMapper = UserMapper.getInstance();
            User user = userMapper.map(resultSet);
            return Optional.of(user);

        } catch (SQLException e) {
            logger.error("Can't find user by ID", e);
            throw new DAOException("Can't find user by ID", e);
        }
    }

    @Override
    public Optional<User> update(User user) throws DAOException {
        Optional<User> toUpdate = findById(user.getId());
        if (!toUpdate.isPresent()) {
            logger.warn("User id=" + user.getId() + " is not presented for update!");
            return Optional.empty();
        }

        try (
                Connection connection = pool.getConnection();
                PreparedStatement updateUser = connection.prepareStatement(UPDATE_USER)
        ) {
            updateUser.setString(1, user.getEmail());
            updateUser.setInt(2, user.getUserRole().ordinal());
            updateUser.setBoolean(3,user.isDeleted());
            updateUser.setString(4,user.getPhone());
            updateUser.setInt(5,user.getId());
            updateUser.execute();
            return Optional.of(user);
        } catch (SQLException e) {
            logger.error("Can't upload new user id= " + user.getId() + " data!", e);
            logger.debug(user);
            throw new DAOException("Can't upload new user data!");
        }
    }

    @Override
    public Optional<User> save(User user) throws DAOException {
        try (
                Connection connection = pool.getConnection();
                PreparedStatement statement = connection.prepareStatement(INSERT_NEW_USER,
                        PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, user.getEmail());
            statement.setString(2, UNDEFINED_USER_PASSWORD);
            statement.setString(3, String.valueOf(user.getUserRole()));
            statement.setBoolean(4,user.isDeleted());
            statement.setString(5,user.getPhone());
            statement.execute();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int userId = generatedKeys.getInt(ID_KEY);
                return findById(userId);
            } else {
                logger.error("Can't get generated keys from Result Set!");
                return Optional.empty();
            }

        } catch (SQLException e) {
            logger.error("Can't save new user!", e);
            logger.debug(user);
            throw new DAOException("Can't save new user!", e);
        }
    }

    @Override
    public Optional<String> findEncryptedPassword(User user) throws DAOException {
        Optional<User> optionalUser = findUniqUser(user);
        if (!optionalUser.isPresent()) {
            return Optional.empty();
        }

        try (Connection connection = pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_USER_PASSWORD)) {

            User targetUser = optionalUser.get();
            statement.setInt(1, targetUser.getId());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String pass = resultSet.getString(PASSWORD);
                return Optional.of(pass);
            } else {
                return Optional.empty();
            }

        } catch (SQLException e) {
            logger.error("Can't find user's password: ", e);
            throw new DAOException("Can't find user's password", e);
        }
    }

    @Override
    public Optional<User> findUniqUser(User user) throws DAOException {
        String email = user.getEmail();
        String phone = user.getPhone();

        if (email != null && !email.isEmpty()) {
            return findByEmail(email);
        } else if (phone != null && !phone.isEmpty()) {
            return findByPhone(phone);
        } else {
            logger.error("Can't find uniq field of provided user!");
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByEmail(String email) throws DAOException {
        Optional<Integer> optionalUserId = findUserIdByEmail(email);
        if (!optionalUserId.isPresent()) {
            return Optional.empty();
        }
        int userId = optionalUserId.get();
        return findById(userId);
    }

    @Override
    public Optional<User> findByPhone(String phone) throws DAOException {
        Optional<Integer> optionalUserId = findUserIdByPhone(phone);
        if (!optionalUserId.isPresent()) {
            return Optional.empty();
        }
        int userId = optionalUserId.get();
        return findById(userId);
    }

    @Override
    public boolean updateUserPassword(User user, String hash) throws DAOException {
        Optional<User> toUpdate = findById(user.getId());
        if (!toUpdate.isPresent()) {
            logger.warn("User id=" + user.getId() + " is not exist!");
            return false;
        }

        try (
                Connection connection = pool.getConnection();
                PreparedStatement statement = connection.prepareStatement(UPDATE_USER_PASSWORD)
        ) {
            statement.setString(1, hash);
            statement.setInt(2, user.getId());
            return statement.execute();
        } catch (SQLException e) {
            logger.error("Can't update password for user id=" + user.getId() + "!", e);
            throw new DAOException("Can't update user password!", e);
        }
    }

    private Optional<Integer> findUserIdByEmail(String email) throws DAOException {
        return findUserId(email, SELECT_USER_ID_BY_EMAIL);
    }

    private Optional<Integer> findUserIdByPhone(String phone) throws DAOException {
        return findUserId(phone, SELECT_USER_ID_BY_PHONE);
    }

    private Optional<Integer> findUserId(String identField, String selectQuery) throws DAOException {
        try (Connection connection = pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(selectQuery)) {
            statement.setString(1, identField);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int userId = resultSet.getInt(ID_USER);
                return Optional.of(userId);
            } else {
                return Optional.empty();
            }

        } catch (SQLException e) {
            logger.error("Can't find user id: ", e);
            throw new DAOException("Can't find user id", e);
        }
    }
}
