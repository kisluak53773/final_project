package com.example.webproject.dao;

import com.example.webproject.dao.entity.Feedback;
import com.example.webproject.exception.DAOException;

public interface FeedbackDAO extends BaseDAO<Feedback>{
    void deleteFeedback(Feedback feedback) throws DAOException;
}
