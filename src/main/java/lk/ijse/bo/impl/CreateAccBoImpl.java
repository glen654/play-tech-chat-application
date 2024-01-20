package lk.ijse.bo.impl;

import lk.ijse.bo.custom.CreateAccBo;
import lk.ijse.dao.DaoFactory;
import lk.ijse.dao.custom.UserDao;
import lk.ijse.dto.UserDto;
import lk.ijse.entity.User;

import java.sql.SQLException;

public class CreateAccBoImpl implements CreateAccBo {

    UserDao userDao = (UserDao) DaoFactory.getDaoFactory().getDao(DaoFactory.DaoTypes.USER);
    @Override
    public boolean saveUser(UserDto dto) throws SQLException {
        return userDao.save(new User(dto.getDisplayName(), dto.getEmail(), dto.getPassword()));
    }
}
