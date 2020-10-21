package project.techClasses;

import dataClasses.AdminSender;
import dataClasses.User;
import notification.AllUsersNotificationImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import services.UserServiceImpl;

public class RegisterAdmin {
    public static void main(String[] args) {

        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        UserServiceImpl userService = (UserServiceImpl) context.getBean("UserService");
        // AllUsersNotificationImpl allUsersNotification = (AllUsersNotificationImpl) context.getBean("AllUserNotificationService");
        //                //Регистрация adminSender (однократно )=====================================================
        System.out.println("Регистрируем adminSender1");
        User admin = new AdminSender();
        admin.setLogin("adminSender1");
        admin.setRole("admin");
        admin.setEmail("aadmhomesavings@gmail.com");
        admin.setPasswordHash("HomeSavings1!");
        admin.setFamily("Администратор2");
        admin.setName("Админ2");
        userService.getUserDAO().save(admin);
        System.out.println( userService.getUserDAO().findUserByLogin("adminSender1"));

    }
}
