package com.example.webproject;

import com.example.webproject.connectionpool.CustomConnectionPool;
import com.example.webproject.exception.DAOException;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Base64;

import static com.example.webproject.dao.ColumnName.*;
import static com.example.webproject.dao.ColumnName.SERVICE_IMAGE;
import static com.example.webproject.dao.TableName.SERVICES;

public class Main {
    private static final String ADD_SERVICE="INSERT INTO"+" "+SERVICES+" ("+PRICE+", "+SERVICE_NAME+", "
            +SERVICE_DESCRIPTION+", "+SERVICE_IMAGE+")"+"VALUES"+" (?,?,?,?)"+";";
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws DAOException, IOException {
        final CustomConnectionPool pool = CustomConnectionPool.getInstance();
        byte[] fileContent = FileUtils.readFileToByteArray(new File("C:/картинки/стрижка ногтей у котовjpg.jpg"));
        String encodedString = Base64.getEncoder().encodeToString(fileContent);
        try (Connection connection = pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(ADD_SERVICE)) {
            statement.setBigDecimal(1, BigDecimal.valueOf(2));
            statement.setString(2,"Стрижка ногтей у котов");
            statement.setString(3,"Лучшая услуга в мире");
            statement.setString(4,encodedString);
            statement.execute();
        } catch (SQLException e) {
            logger.error("Can't save new service!", e);
            throw new DAOException("Can't save new service!", e);
        }
    }
}
