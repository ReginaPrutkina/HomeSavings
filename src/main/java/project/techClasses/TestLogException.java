package project.techClasses;

import log.Logging;
import myException.MyException;
import notification.AllUsersNotificationImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.FileNotFoundException;

public class TestLogException {
    public static void main(String[] args) {

        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        Logging logging = (Logging) context.getBean("LogService");

        try {
            throw new MyException("test ", new MyException("test1 ", new FileNotFoundException("Sample Exception")));
        } catch (MyException e) {
            logging.log(e.getMessage(),e);
            e.printStackTrace();
        }

    }

    }
