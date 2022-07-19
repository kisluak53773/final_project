package com.example.webproject.dao.impl;

import com.example.webproject.connectionpool.CustomConnectionPool;
import com.example.webproject.dao.FeedbackDAO;
import com.example.webproject.dao.entity.Feedback;
import com.example.webproject.dao.mapper.impl.FeedbackMapper;
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
import static com.example.webproject.dao.TableName.FEEDBACKS;

public class FeedbackDAOImpl implements FeedbackDAO {
    private static final String FIND_ALL_FEEDBACKS="SELECT"+ID_FEEDBACK+", "+ID_ORDERED_SERVICE+", "+
            COMMENT+", "+RATING+"FROM"+FEEDBACKS+";";
    private static final String FIND_FEEDBACK_BY_ID="SELECT"+ID_FEEDBACK+", "+ID_ORDERED_SERVICE+", "+
            COMMENT+", "+RATING+"FROM"+FEEDBACKS+
            "WHERE"+ID_FEEDBACK+"=?"+";";
    private static final String UPDATE_FEEDBACK="UPDATE"+FEEDBACKS+"SET"+ID_ORDERED_SERVICE+"=?, "+
            COMMENT+"=?, "+RATING+"=?, "+
            "WHERE"+ID_FEEDBACK+"=?"+";";
    private static final String INSERT_NEW_FEEDBACK="INSERT INTO"+FEEDBACKS+"("+ID_ORDERED_SERVICE+", "
            +COMMENT+RATING+")"+
            "VALUES(?,?,?);";
    private static final String DELETE_COMMENT="DELETE FROM"+FEEDBACKS+"WHERE"+ID_FEEDBACK+"=?"+";";

    private static final Logger logger = LogManager.getLogger(FeedbackDAOImpl.class);
    private final CustomConnectionPool pool = CustomConnectionPool.getInstance();

    @Override
    public List<Feedback> findAll() throws DAOException {
        try (
                Connection connection = pool.getConnection();
                PreparedStatement statement = connection.prepareStatement(FIND_ALL_FEEDBACKS);
                ResultSet resultSet = statement.executeQuery()
        ) {

            List<Feedback> feedbacks = new ArrayList<>();
            FeedbackMapper feedbackMapper= FeedbackMapper.getInstance();
            while (resultSet.next()) {
                Feedback feedback=feedbackMapper.map(resultSet);
                feedbacks.add(feedback);
            }
            return feedbacks;
        } catch (SQLException e) {
            logger.error("Error occurred while loading all feedback", e);
            throw new DAOException("Error occurred while loading feedbacks", e);
        }
    }

    @Override
    public Optional<Feedback> findById(int id) throws DAOException {
        try (Connection connection = pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_FEEDBACK_BY_ID))
        {
            statement.setInt(1,id);
            ResultSet resultSet=statement.executeQuery();
            if (!resultSet.next()) {
                return Optional.empty();
            }
            FeedbackMapper feedbackMapper=FeedbackMapper.getInstance();
            Feedback feedback=feedbackMapper.map(resultSet);
            return Optional.of(feedback);
        } catch (SQLException e) {
            logger.error("Error occurred while loading all feedbacks", e);
            throw new DAOException("Error occurred while loading feedbacks", e);
        }
    }

    @Override
    public Optional<Feedback> update(Feedback feedback) throws DAOException {
        Optional<Feedback> toUpdate = findById(feedback.getId());
        if (!toUpdate.isPresent()) {
            logger.warn("Feedback id=" + feedback.getId() + " is not presented for update!");
            return Optional.empty();
        }

        try (
                Connection connection = pool.getConnection();
                PreparedStatement updateFeedback = connection.prepareStatement(UPDATE_FEEDBACK)
        ) {
            updateFeedback.setInt(1,feedback.getOrderedService().getId());
            updateFeedback.setString(2,feedback.getComment());
            updateFeedback.setInt(3,feedback.getRating().ordinal());
            updateFeedback.setInt(4,feedback.getId());
            updateFeedback.execute();
            return Optional.of(feedback);
        } catch (SQLException e) {
            logger.error("Can't upload new feedback id= " + feedback.getId() + " data!", e);
            logger.debug(feedback);
            throw new DAOException("Can't upload new order data!");
        }
    }

    @Override
    public Optional<Feedback> save(Feedback feedback) throws DAOException {
        try (
                Connection connection = pool.getConnection();
                PreparedStatement statement = connection.prepareStatement(INSERT_NEW_FEEDBACK,
                        PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            statement.setInt(1,feedback.getOrderedService().getId());
            statement.setString(2,feedback.getComment());
            statement.setInt(3,feedback.getRating().ordinal());
            statement.execute();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int feedbackId = generatedKeys.getInt(ID_KEY);
                return findById(feedbackId);
            } else {
                logger.error("Can't get generated keys from Result Set!");
                return Optional.empty();
            }

        } catch (SQLException e) {
            logger.error("Can't save new feedback!", e);
            logger.debug(feedback);
            throw new DAOException("Can't save new feedback!", e);
        }
    }

    @Override
    public void deleteFeedback(Feedback feedback) throws DAOException {
        try (Connection connection = pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_COMMENT))
        {
            statement.setInt(1,feedback.getId());
            statement.executeQuery();
        } catch (SQLException e) {
            logger.error("Can't delete service", e);
            logger.debug(feedback);
            throw new DAOException("Error occurred while deleting of a service", e);
        }
    }
}
