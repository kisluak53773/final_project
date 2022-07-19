package com.example.webproject.dao;

import com.example.webproject.exception.DAOException;

import java.util.List;
import java.util.Optional;

public interface BaseDAO <T>{
    int ID_KEY = 1;
    List<T> findAll() throws DAOException;
    Optional<T> findById(int id) throws DAOException;
    Optional<T> update(T t) throws DAOException;
    Optional<T> save(T t) throws DAOException;
}
