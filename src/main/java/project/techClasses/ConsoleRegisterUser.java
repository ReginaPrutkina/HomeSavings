package project.techClasses;

import dataClasses.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import services.UserServiceImpl;
import java.util.Scanner;
@Service
public class ConsoleRegisterUser {
    @Autowired
    private UserServiceImpl userService;

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
        } while (!userService.checkMailFormat(temp));
        return userService.registerUser(user);
    }

    public User consoleUserAuth(UserServiceImpl userService){
        String login;
        String password;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите логин");
        login = scanner.nextLine();
        System.out.println("Введите пароль");
        password = scanner.nextLine();
        return userService.userAuth(login, password);
    }
}
