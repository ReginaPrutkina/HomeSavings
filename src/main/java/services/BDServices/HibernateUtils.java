package services.BDServices;

import myException.MyException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtils {
        private static SessionFactory sessionFactory;
        static {
            try{
                Configuration configuration = new Configuration().configure();
                sessionFactory = configuration.buildSessionFactory();

            } catch (Exception e) {
                try {
                    throw new MyException("ошибка связи с БД", e);
                } catch (MyException myException) {
                    myException.printStackTrace();
                }
            }
        }

        public static SessionFactory getSessionFactory() {
            return sessionFactory;
        }
    }

