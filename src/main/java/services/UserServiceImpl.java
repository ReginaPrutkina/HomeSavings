package services;

import log.Logging;
import org.springframework.beans.factory.annotation.Autowired;
import dataClasses.AdminSender;
import dataClasses.User;
import org.springframework.stereotype.Service;
import services.BDServices.UserDAO;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private Logging logging;

    public UserDAO getUserDAO() {
        return userDAO;
    }

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public boolean isUserValid(User user) {
        if ((user.getEmail() == null) || (user.getFamily() == null) ||
                (user.getName() == null) || (user.getLogin() == null) ||
                (user.getPasswordHash() == null) || (user.getRole() == null)) {
            System.out.println("Не заполнены обязательные поля");
            logging.log("Не заполнены обязательные поля");
            return false;
        }
        if (userDAO.findUserByLogin(user.getLogin()) != null) {
            System.out.println("В базе есть зарегистированный пользователеь с таким логином");
            logging.log("В базе есть зарегистированный пользователеь с таким логином");
            return false;
        }

        if (userDAO.findUserByLogin(user.getEmail()) != null) {
            System.out.println("В базе есть зарегистированный пользователеь с таким email");
            logging.log("В базе есть зарегистированный пользователеь с таким email");
            return false;
        }
        return true;
    }

    @Override
    public User checkUserPassword(String login, String password) {
        User userCmp = userDAO.findUserByLogin(login);
        if ((userCmp != null) && userCmp.getRole().equals("admin")
                && isAdminPasswordValid(password, (AdminSender) userCmp)) {
            System.out.println("Пользователь авторизован как администратор");
            logging.log("Пользователь авторизован как администратор");
            return (AdminSender) userCmp;
        } else {
            if ((userCmp == null) || (!userCmp.getPasswordHash().equals(Integer.toString(password.hashCode())))) {
                System.out.println("Неверный логин  или пароль");
                logging.log("Неверный логин  или пароль");
                return null;
            }
            System.out.println("Пользователь авторизован");
            return userCmp;
        }
    }

    private boolean isAdminPasswordValid(String password, AdminSender adminSender) {
        return adminSender.getPassword().equals(password);
    }

    @Override
    public User userAuth(String login, String password) {
        User user = checkUserPassword(login, password);
        if (user == null) {
            logging.setUserName(null);
            logging.log("Пользователь с логином " + login + " не авторизован");
        }
        else {
            logging.setUserName(login);
            logging.log("Авторизован пользователь с логином " + login);
        }
        return user;
    }

    @Override
    public User registerUser(User user) {
        if (!isUserValid(user)) {
            logging.setUserName(user.getLogin());
            logging.log("Пользователь не может быть зарегистрирован " + user.toString());
            return null;
        }
        //  записываем в базу
        userDAO.save(user);
        System.out.println("Пользователь успешно прошел регистрацию");
        logging.setUserName(user.getLogin());
        logging.log("Зарегистрирован пользователь " + user.toString());
        return userDAO.findUserByLogin(user.getLogin());
    }

    public boolean checkMailFormat (String email){
        String  emailPattern =
                "^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        return email.matches(emailPattern);
    }
}
