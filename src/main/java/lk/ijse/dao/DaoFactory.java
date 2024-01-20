package lk.ijse.dao;

import lk.ijse.dao.impl.UserDaoImpl;

public class DaoFactory {
    private static DaoFactory daoFactory;

    private DaoFactory() {
    }

    public static DaoFactory getDaoFactory(){
        return daoFactory == null ? daoFactory = new DaoFactory() : daoFactory;
    }

    public enum DaoTypes{
        USER
    }

    public SuperDao getDao(DaoTypes types){
        switch (types){
            case USER:
                return new UserDaoImpl();
            default:
                return null;
        }
    }

}
