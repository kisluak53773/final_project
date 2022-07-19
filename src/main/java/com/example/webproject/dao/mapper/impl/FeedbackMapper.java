package com.example.webproject.dao.mapper.impl;

import com.example.webproject.dao.entity.Feedback;
import com.example.webproject.dao.entity.OrderedService;
import com.example.webproject.dao.mapper.Mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.example.webproject.dao.ColumnName.*;

public class FeedbackMapper implements Mapper {
    public static final FeedbackMapper instance=new FeedbackMapper();

    public static FeedbackMapper getInstance(){
        return instance;
    }

    @Override
    public Feedback map(ResultSet rs) throws SQLException {
        OrderedService orderedService= OrderedService.getBuilder()
                .id(rs.getInt(ID_ORDERED_SERVICE)).build();

        return Feedback.getBuilder()
                .id(rs.getInt(ID_FEEDBACK))
                .orderedService(orderedService)
                .comment(rs.getString(COMMENT))
                .rating(Feedback.Rating.valueOf(rs.getString(RATING).toUpperCase()))
                .build();
    }
}
