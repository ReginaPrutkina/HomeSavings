package services.BDServices;

import currencyService.Currency;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import javax.persistence.Query;
import java.util.Date;
import java.util.List;

@Component
public class CurrencyDAOImpl implements CurrencyDAO{
    @Override
    public void save(Currency currency) {
        Session session = HibernateUtils.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        session.save(currency);
        transaction.commit();
    }

    @Override
    public void update(Currency currency) {
        Session session = HibernateUtils.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        session.update(currency);
        transaction.commit();
    }

    @Override
    public void delete(Currency currency) {
        Session session = HibernateUtils.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        session.delete(currency);
        transaction.commit();
    }

    @Override
    public List<Currency> getCurrencyByCode(String currencyNumCode) {
        Session session = HibernateUtils.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        Query query =  session.createQuery("From Currency where numCode = :param", Currency.class);
        query.setParameter("param",currencyNumCode.trim());
        List<Currency> currencyList = query.getResultList();
        transaction.commit();
        return currencyList;
    }

    @Override
    public List<Currency> getCurrencyByCode(String currencyNumCode, Date data) {
        Session session = HibernateUtils.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        Query query =  session.createQuery("From Currency where numCode = :param1 " +
                "and data = :param2 ", Currency.class);
        query.setParameter("param1",currencyNumCode.trim());
        query.setParameter("param2",data);
        List<Currency> currencyList = query.getResultList();
        transaction.commit();
        return currencyList;
    }

    @Override
    public List<Currency> getCurrencyByCode(String currencyNumCode, Date startDate, Date endDate) {
        Session session = HibernateUtils.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        Query query =  session.createQuery("From Currency where numCode = :param1 " +
                "and data>= :param2 and data <= :param3", Currency.class);
        query.setParameter("param1",currencyNumCode.trim());
        query.setParameter("param2",startDate);
        query.setParameter("param3",endDate);
        List<Currency> currencyList = query.getResultList();
        transaction.commit();
        return currencyList;
    }
}
