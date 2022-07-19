package com.example.webproject.service;

import com.example.webproject.controller.command.Pagination;
import com.example.webproject.dao.entity.Service;
import com.example.webproject.dao.entity.User;
import com.example.webproject.exception.ServiceException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ServiceService {
    List<Service> findAll() throws ServiceException;
    Optional<Service> save(Service announcement) throws ServiceException;
    Pagination<Service> findServices(Map<String, List<String>> parameterMap) throws ServiceException;
    Pagination<Service> findServices(Map<String, List<String>> parameterMap, User user) throws ServiceException;
    Optional<Service> findById(String id) throws ServiceException;
    Optional<Service> updateService(Map<String, List<String>> parameterMap, User user) throws ServiceException;
}
