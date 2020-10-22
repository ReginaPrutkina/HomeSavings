package project;
import currencyService.CurrencyFactory;
import currencyService.GettingCurrency;
import myException.MyException;
import notification.AllUsersNotificationImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class MainFile {
    public static void main(String[] args) {

        //Для отпарвки с аккаута gmail потрбовалось дать разрешение на использование чужими сервисами
        try {
            ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
            AllUsersNotificationImpl allUsersNotification = (AllUsersNotificationImpl) context.getBean("AllUserNotificationService");
 //           GettingCurrency gettingCurrency = (CurrencyFactory) context.getBean("CurrencyFactory");
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
//            System.out.println(gettingCurrency.getCurrency("840"));
//            System.out.println(gettingCurrency.getCurrency("392"));
//            System.out.println(gettingCurrency.getCurrency("840"));

            allUsersNotification.sendNotification(false);


        }catch (MyException myException) {
            myException.printStackTrace();
        }
    }
}
