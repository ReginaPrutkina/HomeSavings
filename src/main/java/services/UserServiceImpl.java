package services;

import log.Logging;
import org.springframework.beans.factory.annotation.Autowired;
import dataClasses.AdminSender;
import dataClasses.User;
import org.springframework.stereotype.Service;

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
        if (user == null)
            logging.log("Пользователь с логином " + login + " не авторизован");
        else
            logging.log("Авторизован пользователь с логином " + login);
        return user;
    }

    public User consoleRegisterUser() {
        String temp;
        String login;
        User user = new User();
        user.setRole("user");
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите Фамилию");
        user.setFamily(scanner.nextLine());

        System.out.println("Введите имя");
        user.setName(scanner.nextLine());

        System.out.println("Введите логин");
        login = scanner.nextLine();
        user.setLogin(login);

        String warning = "";
        do {
            System.out.println(warning);
            System.out.println("Введите пароль");
            user.setPasswordHash(scanner.nextLine());
            System.out.println("Повторите ввод пароля ");
            temp = scanner.nextLine();
            warning = "Введенные пароли не совпали.";
        } while (!user.getPasswordHash().equals(Integer.toString(temp.hashCode())));

        warning = "";
        do {
            System.out.println(warning);
            System.out.println("Введите Ваш email");
            temp = scanner.nextLine();
            user.setEmail(temp);
            warning = "Неверный формат почты";
        } while (!checkMailFormat(temp));
        return registerUser(user);
    }

    @Override
    public User registerUser(User user) {
        if (!isUserValid(user)) {
            logging.log("Пользователь не может быть зарегистрирован " + user.toString());
            return null;
        }
        //  записываем в базу
        userDAO.save(user);
        System.out.println("Пользователь успешно прошел регистрацию");
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