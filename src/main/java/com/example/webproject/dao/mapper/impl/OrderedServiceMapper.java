package com.example.webproject.dao.mapper.impl;

import com.example.webproject.dao.entity.Order;
import com.example.webproject.dao.entity.OrderedService;
import com.example.webproject.dao.entity.Service;
import com.example.webproject.dao.mapper.Mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.example.webproject.dao.ColumnName.*;

public class OrderedServiceMapper implements Mapper {
    public static final OrderedServiceMapper instance=new OrderedServiceMapper();

    public static OrderedServiceMapper getInstance(){
        return instance;
    }

    @Override
    public OrderedService map(ResultSet rs) throws SQLException {
        Order order= Order.getBuilder().id(rs.getInt(ID_ORDER)).build();
        Service service= Service.getBuilder().id(rs.getInt(ID_SERVICE)).build();

        return OrderedService.getBuilder()
                .id(rs.getInt(ID_ORDERED_SERVICE))
                .order(order)
                .service(service)
                .timeAssigned(rs.getDate(TIME_ASSIGNED).toLocalDate())
                .timeRequested(rs.getDate(TIME_REQUESTED).toLocalDate())
                .build();
    }
}
