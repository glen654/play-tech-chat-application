package lk.ijse.dao.impl;

import lk.ijse.dao.SQLUtil;
import lk.ijse.dao.custom.UserDao;
import lk.ijse.dto.UserDto;
import lk.ijse.entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDaoImpl implements UserDao {
    @Override
    public boolean save(User entity) throws SQLException {
        return SQLUtil.
                execute("INSERT INTO user (display_Name,email,password) VALUES (?,?,?)",
                        entity.getDisplayName(),entity.getEmail(),entity.getPassword());
    }

    @Override
    public UserDto search(String displayName, String password) throws SQLException {
        ResultSet resultSet = SQLUtil.execute("SELECT * FROM user WHERE display_name = ? AND password = ?",displayName,password);
        if(resultSet.next()){
            UserDto user = new UserDto();
            user.setDisplayName(resultSet.getString("display_Name"));
            user.setPassword(resultSet.getString("password"));

            return user;
        }else {
            return null;
        }
    }
}
