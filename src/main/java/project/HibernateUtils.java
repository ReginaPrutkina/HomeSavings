package project;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.File;

public class HibernateUtils {
        private static SessionFactory sessionFactory;
        static {
            try{
               // Configuration configuration = new Configuration().configure("D:\\Users\\Lenovo\\IdeaProjects\\java-maven\\src\\main\\resources\\hibernate.cfg.xml");
                Configuration configuration = new Configuration().configure();
                sessionFactory = configuration.buildSessionFactory();

            } catch (Exception e) {
                System.out.println("Исключение!" + e);
            }
        }

        public static SessionFactory getSessionFactory() {
            return sessionFactory;
        }
    }

