package com.example.webproject.dao.impl;

import com.example.webproject.connectionpool.CustomConnectionPool;
import com.example.webproject.dao.BaseDAO;
import com.example.webproject.dao.entity.Order;
import com.example.webproject.dao.mapper.impl.OrderMapper;
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
import static com.example.webproject.dao.TableName.*;

public class OrderDAOImpl implements BaseDAO<Order> {
    private static final String FIND_ALL_ORDERS="SELECT"+ID_ORDER+", "+ID_USER+", "+ORDER_STATE+
            "FORM"+ORDERS+";";
    private static final String FIND_ORDER_BY_ID="SELECT"+ID_ORDER+", "+ID_USER+", "+ORDER_STATE+
            "FORM"+ORDERS+
            "WHERE"+ID_ORDER+"=?"+";";
    private static final String INSERT_NEW_ORDER="INSERT INTO "+USERS+"("+ID_ORDER+", "+ORDER_STATE+")"+
            "VALUES(?,?);";
    private static final String UPDATE_ORDER="UPDATE"+ORDERS+"SET"+ID_USER+"=?, "+ORDER_STATE+"=?, "+
            "WHERE"+ID_ORDER+"=?"+";";

    private static final Logger logger = LogManager.getLogger(OrderDAOImpl.class);
    private final CustomConnectionPool pool = CustomConnectionPool.getInstance();
    @Override
    public List<Order> findAll() throws DAOException {
        try (
                Connection connection = pool.getConnection();
                PreparedStatement statement = connection.prepareStatement(FIND_ALL_ORDERS);
                ResultSet resultSet = statement.executeQuery()
        ) {

            List<Order> orders = new ArrayList<>();
            OrderMapper orderMapper= OrderMapper.getInstance();
            while (resultSet.next()) {
                Order order=orderMapper.map(resultSet);
                orders.add(order);
            }
            return orders;
        } catch (SQLException e) {
            logger.error("Error occurred while loading all orders", e);
            throw new DAOException("Error occurred while loading orders", e);
        }
    }

    @Override
    public Optional<Order> findById(int id) throws DAOException {
        try (Connection connection = pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ORDER_BY_ID))
        {
            statement.setInt(1,id);
            ResultSet resultSet=statement.executeQuery();
            if (!resultSet.next()) {
                return Optional.empty();
            }
            OrderMapper mapper=OrderMapper.getInstance();
            Order order=mapper.map(resultSet);
            return Optional.of(order);
        } catch (SQLException e) {
            logger.error("Error occurred while loading all orders", e);
            throw new DAOException("Error occurred while loading orders", e);
        }
    }

    @Override
    public Optional<Order> update(Order order) throws DAOException {
        Optional<Order> toUpdate = findById(order.getId());
        if (!toUpdate.isPresent()) {
            logger.warn("Order id=" + order.getId() + " is not presented for update!");
            return Optional.empty();
        }

        try (
                Connection connection = pool.getConnection();
                PreparedStatement updateOrder = connection.prepareStatement(UPDATE_ORDER)
        ) {
            updateOrder.setInt(1,order.getUser().getId());
            updateOrder.setInt(2,order.getOrderState().ordinal());
            updateOrder.execute();
            return Optional.of(order);
        } catch (SQLException e) {
            logger.error("Can't upload new order id= " + order.getId() + " data!", e);
            logger.debug(order);
            throw new DAOException("Can't upload new order data!");
        }
    }

    @Override
    public Optional<Order> save(Order order) throws DAOException {
        try (
                Connection connection = pool.getConnection();
                PreparedStatement statement = connection.prepareStatement(INSERT_NEW_ORDER,
                        PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            statement.setInt(1,order.getId());
            statement.setInt(2,order.getOrderState().ordinal());
            statement.execute();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int orderId = generatedKeys.getInt(ID_KEY);
                return findById(orderId);
            } else {
                logger.error("Can't get generated keys from Result Set!");
                return Optional.empty();
            }

        } catch (SQLException e) {
            logger.error("Can't save new order!", e);
            logger.debug(order);
            throw new DAOException("Can't save new order!", e);
        }
    }
}
