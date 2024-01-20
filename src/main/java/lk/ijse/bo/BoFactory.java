package lk.ijse.bo;

import lk.ijse.bo.impl.CreateAccBoImpl;
import lk.ijse.bo.impl.LoginBoImpl;

public class BoFactory {
    private static BoFactory boFactory;

    private BoFactory() {
    }

    public static BoFactory getBoFactory(){
        return boFactory == null ? boFactory = new BoFactory() : boFactory;
    }

    public enum BoTypes{
        CREATE_ACCOUNT,LOGIN
    }

    public SuperBo getBo(BoTypes types){
        switch (types){
            case LOGIN:
                return new LoginBoImpl();
            case CREATE_ACCOUNT:
                return new CreateAccBoImpl();
            default:
                return null;
        }
    }
}
