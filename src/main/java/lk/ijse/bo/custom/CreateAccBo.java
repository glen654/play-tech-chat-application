package lk.ijse.bo.custom;

import lk.ijse.bo.SuperBo;
import lk.ijse.dto.UserDto;

import java.sql.SQLException;

public interface CreateAccBo extends SuperBo {
    boolean saveUser(UserDto dto) throws SQLException;
}
