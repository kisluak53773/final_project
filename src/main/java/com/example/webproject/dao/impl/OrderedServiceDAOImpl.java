package com.example.webproject.dao.impl;

import com.example.webproject.connectionpool.CustomConnectionPool;
import com.example.webproject.dao.BaseDAO;
import com.example.webproject.dao.entity.OrderedService;
import com.example.webproject.dao.mapper.impl.OrderedServiceMapper;
import com.example.webproject.exception.DAOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.webproject.dao.ColumnName.*;
import static com.example.webproject.dao.TableName.*;

public class OrderedServiceDAOImpl implements BaseDAO<OrderedService> {
    private static final String FIND_ALL_ORDERED_SERVICES="SELECT"+ID_ORDERED_SERVICE+", "+ID_ORDER+
            ", "+ID_SERVICE+", "+TIME_ASSIGNED+", "+TIME_REQUESTED+
            "FROM"+ORDERED_SERVICES+";";
    private static final String FIND_ORDERED_SERVICE_BY_ID="SELECT"+ID_ORDERED_SERVICE+", "+ID_ORDER+
            ", "+ID_SERVICE+", "+TIME_ASSIGNED+", "+TIME_REQUESTED+
            "FROM"+ORDERED_SERVICES+
            "WHERE"+ID_ORDERED_SERVICE+"=?;";
    private static final String UPDATE_ORDERED_SERVICE="UPDATE"+FEEDBACKS+"SET"+ID_ORDER+"=?, "+
            ID_SERVICE+"=?, "+TIME_ASSIGNED+"=?, "+TIME_REQUESTED+"=?, "+
            "WHERE"+ID_ORDERED_SERVICE+"=?;";
    private static final String INSERT_NEW_ORDERED_SERVICE="INSERT INTO"+ORDERED_SERVICES+"("+ID_ORDER+
            ", "+ID_SERVICE+", "+TIME_ASSIGNED+", "+TIME_REQUESTED+")"+
            "VALUES(?,?,?,?);";

    private static final Logger logger = LogManager.getLogger(OrderedServiceDAOImpl.class);
    private final CustomConnectionPool pool = CustomConnectionPool.getInstance();
    @Override
    public List<OrderedService> findAll() throws DAOException {
        try (
                Connection connection = pool.getConnection();
                PreparedStatement statement = connection.prepareStatement(FIND_ALL_ORDERED_SERVICES);
                ResultSet resultSet = statement.executeQuery()
        ) {

            List<OrderedService> orderedServices = new ArrayList<>();
            OrderedServiceMapper orderedServiceMapper=OrderedServiceMapper.getInstance();
            while (resultSet.next()) {
                OrderedService orderedService=orderedServiceMapper.map(resultSet);
                orderedServices.add(orderedService);
            }
            return orderedServices;
        } catch (SQLException e) {
            logger.error("Error occurred while loading all ordered services", e);
            throw new DAOException("Error occurred while loading ordered services", e);
        }
    }

    @Override
    public Optional<OrderedService> findById(int id) throws DAOException {
        try (Connection connection = pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ORDERED_SERVICE_BY_ID))
        {
            statement.setInt(1,id);
            ResultSet resultSet=statement.executeQuery();
            if (!resultSet.next()) {
                return Optional.empty();
            }
            OrderedServiceMapper orderedServiceMapper=OrderedServiceMapper.getInstance();
            OrderedService orderedService=orderedServiceMapper.map(resultSet);
            return Optional.of(orderedService);
        } catch (SQLException e) {
            logger.error("Error occurred while loading all ordered services", e);
            throw new DAOException("Error occurred while loading ordered services", e);
        }
    }

    @Override
    public Optional<OrderedService> update(OrderedService orderedService) throws DAOException {
        Optional<OrderedService> toUpdate = findById(orderedService.getId());
        if (!toUpdate.isPresent()) {
            logger.warn("Ordered service id=" + orderedService.getId() + " is not presented for update!");
            return Optional.empty();
        }

        try (
                Connection connection = pool.getConnection();
                PreparedStatement updateOrderedService = connection.prepareStatement(UPDATE_ORDERED_SERVICE)
        ) {
            updateOrderedService.setInt(1,orderedService.getOrder().getId());
            updateOrderedService.setInt(2,orderedService.getService().getId());
            updateOrderedService.setDate(3, Date.valueOf(orderedService.getTimeAssigned()));
            updateOrderedService.setDate(4, Date.valueOf(orderedService.getTimeRequested()));
            updateOrderedService.setInt(5,orderedService.getId());
            updateOrderedService.execute();
            return Optional.of(orderedService);
        } catch (SQLException e) {
            logger.error("Can't upload new ordered service id= " + orderedService.getId() + " data!", e);
            logger.debug(orderedService);
            throw new DAOException("Can't upload new ordered service data!");
        }
    }

    @Override
    public Optional<OrderedService> save(OrderedService orderedService) throws DAOException {
        try (
                Connection connection = pool.getConnection();
                PreparedStatement statement = connection.prepareStatement(INSERT_NEW_ORDERED_SERVICE,
                        PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            statement.setInt(1,orderedService.getOrder().getId());
            statement.setInt(2,orderedService.getService().getId());
            statement.setDate(3, Date.valueOf(orderedService.getTimeAssigned()));
            statement.setDate(4, Date.valueOf(orderedService.getTimeRequested()));
            statement.execute();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int orderedServiceId = generatedKeys.getInt(ID_KEY);
                return findById(orderedServiceId);
            } else {
                logger.error("Can't get generated keys from Result Set!");
                return Optional.empty();
            }

        } catch (SQLException e) {
            logger.error("Can't save new ordered service!", e);
            logger.debug(orderedService);
            throw new DAOException("Can't save new ordered service!", e);
        }
    }
}
