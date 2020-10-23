package services;

import log.Logging;
import org.springframework.beans.factory.annotation.Autowired;
import dataClasses.AdminSender;
import dataClasses.User;
import org.springframework.stereotype.Service;
import services.BDServices.UserDAO;

import java.util.Scanner;

@Service
public class UserServiceImpl implements UserService {

    private UserDAO userDAO;

    @Autowired
    private Logging logging;

    @Autowired
    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public boolean isUserValid(User user) {
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
    public User checkUserPassword(String login, String password) {
        User userCmp = userDAO.findUserByLogin(login);
        if ((userCmp != null) && userCmp.getRole().equals("admin")
                && isAdminPasswordValid(login, password, (AdminSender) userCmp)) {
            System.out.println("Пользователь авторизован как администратор");
            return (AdminSender) userCmp;
        } else {
            if ((userCmp == null) || (!userCmp.getPasswordHash().equals(Integer.toString(password.hashCode())))) {
                System.out.println("Неверный логин  или пароль");
                return null;
            }
            System.out.println("Пользователь авторизован");
            return userCmp;
        }
    }

    private boolean isAdminPasswordValid(String login, String password, AdminSender adminSender) {
        return adminSender.getPassword().equals(password);
    }

    public User consoleUserAuth(){
        String login;
        String password;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите логин");
        login = scanner.nextLine();
        System.out.println("Введите пароль");
        password = scanner.nextLine();
        return userAuth(login, password);
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
        logging.log("Зарегистриован пользователь " + user.toString());
        return userDAO.findUserByLogin(user.getLogin());
    }

    public boolean checkMailFormat (String email){
        String  emailPattern =
                "^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        return email.matches(emailPattern);
    }
}
