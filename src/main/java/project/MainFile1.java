package project;

import dataClasses.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import services.UserDAO;
import services.UserDAOImpl;

public class MainFile1 {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        UserDAO userDAO = (UserDAOImpl) context.getBean("UserDAO");
        //UserService userService = new UserServiceImpl(userDAO);
        // User user2 = userService.registerUser();
        //  User user2 = userService.userAuth();
        User user2 = userDAO.findUserByLogin("shvartz1");


        //  userDAO.save(user2);
        if (user2 != null) {
            System.out.println("Удаляем " + user2.getName() + " " + user2.getFamily());

           userDAO.delete(user2);


    }

    }
}
