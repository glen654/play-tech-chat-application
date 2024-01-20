package lk.ijse.bo.custom;

import lk.ijse.bo.SuperBo;
import lk.ijse.dto.UserDto;

import java.sql.SQLException;

public interface LoginBo extends SuperBo {
    UserDto searchUser(String displayName, String password) throws SQLException;
}
