package com.example.webproject.dao.mapper.impl;

import com.example.webproject.dao.entity.Order;
import com.example.webproject.dao.entity.User;
import com.example.webproject.dao.mapper.Mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import static com.example.webproject.dao.ColumnName.*;

public class OrderMapper implements Mapper {
    public static final OrderMapper instance=new OrderMapper();

    public static OrderMapper getInstance(){
        return instance;
    }

    @Override
    public Order map(ResultSet rs) throws SQLException {
        User user = User.getBuilder()
                .id(rs.getInt(ID_USER)).build();

        return  Order.getBuilder().
                id(rs.getInt(ID_ORDER))
                .user(user).
                orderState(Order.OrderState.valueOf(rs.getString(ORDER_STATE).toUpperCase()))
                .build();
    }
}
