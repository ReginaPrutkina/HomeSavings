package project;

import java.util.List;
import java.util.Scanner;

public class UserServiceImpl implements UserService{
    private UserDAO userDAO;

    public UserServiceImpl(UserDAO userDAO) {
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
        if ((userCmp == null) || (userCmp.getPasswordHash() != password.hashCode()) ){
            System.out.println("Неверный логин  или пароль");
            return null;
        }
        System.out.println("Пользователь авторизован");
        return userCmp;
    }

    @Override
    public User userAuth() {
        String login;
        String password;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите логин");
        login = scanner.nextLine();
        System.out.println("Введите пароль");
        password = scanner.nextLine();
        return checkUserPassword(login,password);
    }

    @Override
    public User registerUser() {
        String temp;
        String login;
        User user = new User();
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
        } while (user.getPasswordHash() != temp.hashCode());


        // проверить формат
        warning = "";
        do {
            System.out.println(warning);
            System.out.println("Введите Ваш email");
            temp = scanner.nextLine();
            user.setEmail(temp);
            warning = "Неверный формат почты";
        }while (!checkMailFormat(temp));

        if (!isUserValid(user))
            return null;

        //  записываем в базу
        userDAO.save(user);
        System.out.println("Пользователь успешно прошел регистрацию");
        return userDAO.findUserByLogin(login);
    }

    public boolean checkMailFormat (String email){
        return true;
    }

}
