package com.example.webproject.connectionpool;

import com.example.webproject.exception.DAOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {
    private static final Logger logger = LogManager.getLogger(ConnectionFactory.class);

    private static final String PROPERTIES_PATH = "/prop/db.properties";
    private static final String DRIVER_NAME = "driver";
    private static final String URL_PROPERTY_NAME = "url";
    private static final Properties prop = new Properties();

    private static ConnectionFactory instance;

    static {
        String driverName = null;
        try (InputStream inputStream = ConnectionFactory.class.getResourceAsStream(PROPERTIES_PATH)) {
            prop.load(inputStream);
            driverName = (String) prop.get(DRIVER_NAME);
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            logger.fatal("Can't register driver: " + driverName, e);
            throw new ExceptionInInitializerError("Can't register driver: " + driverName);
        } catch (IOException e) {
            logger.fatal("Can't load db config: ", e);
            throw new ExceptionInInitializerError("Can't load properties file");
        }
    }

    ConnectionFactory() {
    }

    static ConnectionFactory getInstance() {
        if (instance == null) {
            instance = new ConnectionFactory();
        }
        return instance;
    }

    Connection getConnection() throws DAOException {
        try {
            Connection connection = DriverManager.getConnection(prop.getProperty(URL_PROPERTY_NAME), prop);
            return new ProxyConnection(connection);
        } catch (SQLException e) {
            logger.fatal("Can't reach database!", e);
            throw new DAOException("Connection to database failed!", e);
        }
    }
}
