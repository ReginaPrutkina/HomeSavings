package project;

import java.util.List;

public class UserServiceImpl implements UserService{

    @Override
    public boolean isUserValid(User user, UserDAO userDAO) {
        if (user.getEmail().isEmpty() || user.getFamily().isEmpty() ||
                user.getName().isEmpty() || user.getLogin().isEmpty()) {
            System.out.println("Не заполнены обязательные поля");
            return false;
        }
        if (userDAO.findUserByLogin(user.getLogin()) != null) {
            System.out.println("В базе есть зарегистированный пользователеь с таким логином");
            return false;
        }

        if (userDAO.findUserByLogin(user.getEmail()) != null) {
            System.out.println("В базе есть зарегистированный пользователеь с таким email");
            return false;
        }
        return true;
    }

    @Override
    public boolean checkUserPassword(User user, UserDAO userDAO) {
        User userCmp = userDAO.findUserByLogin(user.getEmail());
        if ((userCmp == null) || (userCmp.getPasswordHash() != user.getPasswordHash()) ){
            System.out.println("Неверный логин  или пароль");
            return false;
        }
        return true;
    }

}
