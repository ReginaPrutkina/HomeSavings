package project;
import currencyService.CurrencyFactory;
import currencyService.GettingCurrency;
import log.Logging;
import myException.MyException;
import notification.AllUsersNotificationImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.FileNotFoundException;


public class MainFile {
    public static void main(String[] args) {

        //Для отпарвки с аккаута gmail потрбовалось дать разрешение на использование чужими сервисами

            ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
            AllUsersNotificationImpl allUsersNotification = (AllUsersNotificationImpl) context.getBean("AllUserNotificationService");
            Logging logging = (Logging) context.getBean("LogService");
        try {

            allUsersNotification.sendNotification(false);

        }catch (MyException myException) {
            logging.log(myException.getMessage(),myException);
            myException.printStackTrace();
        }
    }
}
