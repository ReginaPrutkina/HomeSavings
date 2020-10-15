package project;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtils {
        private static SessionFactory sessionFactory;
        static {
            try{
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

