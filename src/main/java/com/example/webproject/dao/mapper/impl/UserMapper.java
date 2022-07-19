package com.example.webproject.dao.mapper.impl;

import com.example.webproject.dao.entity.User;
import com.example.webproject.dao.mapper.Mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.example.webproject.dao.ColumnName.*;

public class UserMapper implements Mapper {
    private static final UserMapper instance = new UserMapper();

    public static UserMapper getInstance(){
        return instance;
    }

    @Override
    public User map(ResultSet rs) throws SQLException {
        return User.getBuilder().id(rs.getInt(ID_USER)).
                email(rs.getString(EMAIL)).
                userRole(User.UserRole.valueOf(rs.getString(USER_ROLE))).
                isDeleted(rs.getBoolean(IS_DELETED)).
                phone(rs.getString(PHONE)).build();
    }
}
