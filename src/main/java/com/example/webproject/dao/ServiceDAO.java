package com.example.webproject.dao;

import com.example.webproject.dao.entity.Service;
import com.example.webproject.exception.DAOException;

import java.util.Optional;

public interface ServiceDAO extends BaseDAO<Service>{
    Optional<Service> findByServiceName(String name) throws DAOException;
    void deleteService(Service service) throws DAOException;
}
