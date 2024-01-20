package lk.ijse.bo.impl;

import lk.ijse.bo.custom.LoginBo;
import lk.ijse.dao.DaoFactory;
import lk.ijse.dao.custom.UserDao;
import lk.ijse.dto.UserDto;

import java.sql.SQLException;

public class LoginBoImpl implements LoginBo {

    UserDao userDao = (UserDao) DaoFactory.getDaoFactory().getDao(DaoFactory.DaoTypes.USER);
    @Override
    public UserDto searchUser(String displayName, String password) throws SQLException {
        return userDao.search(displayName,password);
    }
}
