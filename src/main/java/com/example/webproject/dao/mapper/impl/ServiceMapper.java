package com.example.webproject.dao.mapper.impl;

import com.example.webproject.dao.entity.Service;
import com.example.webproject.dao.mapper.Mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.example.webproject.dao.ColumnName.*;

public class ServiceMapper implements Mapper {
    public static final ServiceMapper instance=new ServiceMapper();

    public static ServiceMapper getInstance(){
        return instance;
    }

    @Override
    public Service map(ResultSet rs) throws SQLException {
        return Service.getBuilder().id(rs.getInt(ID_SERVICE))
                .price(rs.getBigDecimal(PRICE))
                .serviceName(rs.getString(SERVICE_NAME))
                .serviceDescription(rs.getString(SERVICE_DESCRIPTION))
                .serviceImage(rs.getString(SERVICE_IMAGE))
                .build();
    }
}
