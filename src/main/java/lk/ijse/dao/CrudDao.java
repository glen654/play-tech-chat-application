package lk.ijse.dao;

import lk.ijse.dto.UserDto;


import java.sql.SQLException;

public interface CrudDao<T> extends SuperDao {
    boolean save(T entity) throws SQLException;

    UserDto search(String displayName, String password) throws SQLException;
}
