package com.smartbanking.dao;

import java.sql.SQLException;
import java.util.List;

public interface CrudDAO<T> {
    boolean create(T entity) throws SQLException;
    boolean update(T entity) throws SQLException;
    boolean delete(int id) throws SQLException;
    List<T> findAllByUserId(int userId) throws SQLException;
}
