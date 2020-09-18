package project;

import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Parameter;
import javax.persistence.Query;
import java.util.List;

public class UserDAOImpl implements UserDAO{
    @Override
    public User findUserById(long user_id) {
        return HibernateUtils.getSessionFactory().openSession().get(User.class, user_id);
    }

    @Override
    public User findUserByLogin(String login) {
        Query query =  HibernateUtils.getSessionFactory().openSession().createQuery("From User where login = :param");
        query.setParameter("param",login.trim().toLowerCase());
        List<User> users = query.getResultList();
        if (users.isEmpty())
            return null;
        return users.get(0);
    }

    @Override
    public User findUserByEmail(String email) {
        Query query =  HibernateUtils.getSessionFactory().openSession().createQuery("From User where email = :param");
        query.setParameter("param",email.trim().toLowerCase());
        List<User> users = query.getResultList();
        if (users.isEmpty())
            return null;
        return users.get(0);
    }

    @Override
    public void save(User user) {
        Session session = HibernateUtils.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.save(user);
        transaction.commit();
        session.close();
    }

    @Override
    public void delete(User user) {
        Session session = HibernateUtils.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.delete(user);
        transaction.commit();
        session.close();
    }

    @Override
    public void update(User user) {
        Session session = HibernateUtils.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.update(user);
        transaction.commit();
        session.close();
    }

    @Override
    public List<User> findAll() {
        return HibernateUtils.getSessionFactory().openSession().createQuery("From User").list();
    }
}
