package project.techClasses;

import log.Logging;
import homeSavingsException.HomeSavingsException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.FileNotFoundException;

public class TestLogException {
    public static void main(String[] args) {

        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        Logging logging = (Logging) context.getBean("LogService");

        try {
            throw new HomeSavingsException("test ", new HomeSavingsException("test1 ", new FileNotFoundException("Sample Exception")));
        } catch (HomeSavingsException e) {
            logging.log(e.getMessage(),e);
            e.printStackTrace();
        }

    }

    }
