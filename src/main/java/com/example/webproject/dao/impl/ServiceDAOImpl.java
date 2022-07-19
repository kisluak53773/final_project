package com.example.webproject.dao.impl;

import com.example.webproject.connectionpool.CustomConnectionPool;
import com.example.webproject.dao.ServiceDAO;
import com.example.webproject.dao.entity.Service;
import com.example.webproject.dao.mapper.impl.ServiceMapper;
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
import static com.example.webproject.dao.TableName.SERVICES;

public class ServiceDAOImpl implements ServiceDAO {
    private static final String FIND_SERVICES="SELECT"+ID_SERVICE+", "+PRICE+", "+SERVICE_NAME+", "+
            SERVICE_DESCRIPTION+", "+SERVICE_IMAGE+
            "FROM"+SERVICES+";";
    private static final String FIND_SERVICE_BY_ID="SELECT"+ID_SERVICE+", "+PRICE+", "+SERVICE_NAME+", "+
            SERVICE_DESCRIPTION+", "+SERVICE_IMAGE+
            "FROM"+SERVICES+
            "WHERE"+ID_SERVICE+"=?"+";";
    private static final String UPDATE_SERVICE="UPDATE"+SERVICES+"SET"+PRICE+"=?, "+SERVICE_NAME+"=?, "
            +SERVICE_DESCRIPTION+"=?, "+SERVICE_IMAGE+"=?, "+
            "WHERE"+ID_SERVICE+"=?, "+";";
    private static final String ADD_SERVICE="INSERT INTO"+SERVICES+" ("+PRICE+", "+SERVICE_NAME+", "
            +SERVICE_DESCRIPTION+", "+SERVICE_IMAGE+")"+"VALUES"+" (?,?,?,?)"+";";
    private static final String FIND_SERVICE_BY_NAME="SELECT"+ID_SERVICE+", "+PRICE+", "+SERVICE_NAME+", "+
            SERVICE_DESCRIPTION+", "+SERVICE_IMAGE+
            "FROM"+SERVICES+
            "WHERE"+SERVICE_NAME+"=?"+";";
    private static final String DELETE_SERVICE="DELETE FROM"+SERVICES+"WHERE"+ID_USER+"=?"+";";

    private static final Logger logger = LogManager.getLogger(ServiceDAOImpl.class);
    private final CustomConnectionPool pool = CustomConnectionPool.getInstance();

    @Override
    public List<Service> findAll() throws DAOException {
        try (
                Connection connection = pool.getConnection();
                PreparedStatement statement = connection.prepareStatement(FIND_SERVICES);
                ResultSet resultSet = statement.executeQuery()
        ) {

            List<Service> services = new ArrayList<>();
            ServiceMapper serviceMapper= ServiceMapper.getInstance();
            while (resultSet.next()) {
                Service service=serviceMapper.map(resultSet);
                services.add(service);
            }
            return services;
        } catch (SQLException e) {
            logger.error("Error occurred while loading all services", e);
            throw new DAOException("Error occurred while loading services", e);
        }
    }

    @Override
    public Optional<Service> findById(int id) throws DAOException {
        try (Connection connection = pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_SERVICE_BY_ID))
        {
            statement.setInt(1,id);
            ResultSet resultSet=statement.executeQuery();
            if (!resultSet.next()) {
                return Optional.empty();
            }
            ServiceMapper mapper=ServiceMapper.getInstance();
            Service service=mapper.map(resultSet);
            return Optional.of(service);
        } catch (SQLException e) {
            logger.error("Error occurred while loading all services", e);
            throw new DAOException("Error occurred while loading services", e);
        }
    }

    @Override
    public Optional<Service> update(Service service) throws DAOException {
        Optional<Service> toUpdate = findById(service.getId());
        if (!toUpdate.isPresent()) {
            logger.warn("Service id=" + service.getId() + " is not presented for update!");
            return Optional.empty();
        }

        try (Connection connection = pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_SERVICE)) {
            statement.setBigDecimal(1,service.getPrice());
            statement.setString(2,service.getServiceName());
            statement.setString(3,service.getServiceDescription());
            statement.setString(4,service.getServiceImage());
            statement.setInt(5,service.getId());
            statement.execute();
            return Optional.of(service);
        } catch (SQLException e) {
            logger.error("Can't upload new service id= " + service.getId() + " data!", e);
            logger.debug(service);
            throw new DAOException("Can't upload new order data!");
        }
    }

    @Override
    public Optional<Service> save(Service service) throws DAOException {
        try (Connection connection = pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(ADD_SERVICE,
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setBigDecimal(1,service.getPrice());
            statement.setString(2,service.getServiceName());
            statement.setString(3,service.getServiceDescription());
            statement.setString(4,service.getServiceImage());
            statement.execute();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int serviceId = generatedKeys.getInt(ID_KEY);
                return findById(serviceId);
            } else {
                logger.error("Can't get generated keys from Result Set!");
                return Optional.empty();
            }
        } catch (SQLException e) {
            logger.error("Can't save new service!", e);
            logger.debug(service);
            throw new DAOException("Can't save new service!", e);
        }
    }

    @Override
    public Optional<Service> findByServiceName(String name) throws DAOException {
        try (Connection connection = pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_SERVICE_BY_NAME))
        {
            statement.setString(1,name);
            ResultSet resultSet=statement.executeQuery();
            if (!resultSet.next()) {
                return Optional.empty();
            }
            ServiceMapper mapper=ServiceMapper.getInstance();
            Service service=mapper.map(resultSet);
            return Optional.of(service);
        } catch (SQLException e) {
            logger.error("Error occurred while loading all services", e);
            throw new DAOException("Error occurred while loading services", e);
        }
    }

    @Override
    public void deleteService(Service service) throws DAOException {
        try (Connection connection = pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_SERVICE))
        {
            statement.setInt(1,service.getId());
            statement.executeQuery();
        } catch (SQLException e) {
            logger.error("Can't delete service", e);
            logger.debug(service);
            throw new DAOException("Error occurred while deleting of a service", e);
        }
    }
}
