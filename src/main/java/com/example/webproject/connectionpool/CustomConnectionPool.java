package com.example.webproject.connectionpool;

import com.example.webproject.exception.DAOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CustomConnectionPool {
    private static final Logger logger = LogManager.getLogger(CustomConnectionPool.class);

    private static final AtomicBoolean instanceCreated = new AtomicBoolean(false);
    private static final Lock lock = new ReentrantLock(true);
    private static final int INITIAL_POOL_SIZE = 15;

    private static CustomConnectionPool instance = null;

    private final LinkedBlockingQueue<ProxyConnection> freeConnections;
    private final LinkedBlockingQueue<ProxyConnection> busyConnections;

    private CustomConnectionPool() {
        freeConnections = new LinkedBlockingQueue<>(INITIAL_POOL_SIZE);
        busyConnections = new LinkedBlockingQueue<>(INITIAL_POOL_SIZE);
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        try {
            for (int i = 0; i < INITIAL_POOL_SIZE; i++) {
                freeConnections.offer((ProxyConnection) connectionFactory.getConnection());
            }
            if (freeConnections.isEmpty()) {
                logger.fatal("Unable to initialize connection pool");
                throw new RuntimeException("Unable to initialize connection pool");
            }
            logger.info("Connection pool initialized successfully.");
        } catch (DAOException e) {
            logger.fatal("Unable to initialize connection pool: ", e);
            throw new RuntimeException("Unable to initialize connection pool: ", e);
        }
    }

    public static CustomConnectionPool getInstance() {
        if (!instanceCreated.get()) {
            lock.lock();
            try {
                if (instance == null) {
                    instance = new CustomConnectionPool();
                    instanceCreated.set(true);
                }
            } finally {
                lock.unlock();
            }
        }
        return instance;
    }

    public Connection getConnection() {
        ProxyConnection connection = null;
        try {
            connection = freeConnections.take();
            busyConnections.put(connection);
        } catch (InterruptedException e) {
            logger.error("Error occurred while supplying connection:", e);
        }
        return connection;
    }

    public boolean releaseConnection(Connection connection) {
        if (connection instanceof ProxyConnection) {
            ProxyConnection proxyConnection = (ProxyConnection) connection;
            try {
                boolean removed = busyConnections.remove(proxyConnection);
                if (removed) {
                    freeConnections.put(proxyConnection);
                }
                return removed;
            } catch (InterruptedException e) {
                logger.error("Connection could not being released!");
                return false;
            }
        } else {
            logger.error("Invalid connection could not being released: not instanceof" + CustomConnectionPool.class);
            return false;
        }
    }

    public void destroyPool() throws DAOException {
        for (int i = 0; i < INITIAL_POOL_SIZE; i++) {
            try {
                ProxyConnection connection = freeConnections.take();
                connection.reallyClose();
            } catch (SQLException | InterruptedException e) {
                logger.error("Error occurred while destroying pool: ", e);
                throw new DAOException("Error occurred while destroying connection pool!");
            }
        }
        deregisterDriver();
        logger.info("Pool destroyed successfully!");
    }

    private void deregisterDriver() {
        DriverManager.getDrivers().asIterator().forEachRemaining(driver -> {
            try {
                DriverManager.deregisterDriver(driver);
            } catch (SQLException e) {
                logger.error("Error occurred while deregister JDBC driver: ", e);
            }
        });
    }

    @Override
    public String toString() {
        return "ConnectionPool{" +
                "freeConnections=" + freeConnections +
                ", busyConnections=" + busyConnections +
                '}';
    }
}
