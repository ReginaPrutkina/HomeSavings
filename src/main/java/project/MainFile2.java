package project;

import log.LogToFile;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class MainFile2 {
    public static void main(String[] args) {

            LogToFile log = new LogToFile();
            log.setLogFolder(".\\log");
            log.setLogFileName("log.txt");
            log.log("'это текст");

    }
}
