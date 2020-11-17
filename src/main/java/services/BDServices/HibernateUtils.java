package services.BDServices;

import homeSavingsException.HomeSavingsException;
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
                    throw new HomeSavingsException("ошибка связи с БД", e);
                } catch (HomeSavingsException homeSavingsException) {
                    homeSavingsException.printStackTrace();
                }
            }
        }

        public static SessionFactory getSessionFactory() {
            return sessionFactory;
        }
    }

