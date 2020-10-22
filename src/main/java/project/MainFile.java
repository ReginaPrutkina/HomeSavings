package project;
import myException.MyException;
import notification.AllUsersNotificationImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import services.UserServiceImpl;

public class MainFile {
    public static void main(String[] args) {

        //Для отпарвки с аккаута gmail потрбовалось дать разрешение на использование чужими сервисами
        try {
            ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
            UserServiceImpl userService = (UserServiceImpl) context.getBean("UserService");
            AllUsersNotificationImpl allUsersNotification = (AllUsersNotificationImpl) context.getBean("AllUserNotificationService");

//            //Регистрация пользователя ==================================================================
 //             userService.consoleRegisterUser();
//
//            // Аутентификация ползователя ================================================================
 //               User user1 = userService.consoleUserAuth();
//           // User user1 = userDAO.findUserByLogin("petrov");
 //           if ( user1 != null)
 //               System.out.println("Приятной работы, "+ user1.getName() + " " + user1.getFamily());
//            String userLogin = user1.getLogin();
//            UserDAOImpl userDAO = (UserDAOImpl) context.getBean("UserDAO") ;
//

            allUsersNotification.sendNotification(true);


        }catch (MyException myException) {
            myException.printStackTrace();
        }
    }
}
