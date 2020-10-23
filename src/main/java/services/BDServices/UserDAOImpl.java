package services.BDServices;

import org.hibernate.Session;
import org.hibernate.Transaction;
import dataClasses.User;
import org.springframework.stereotype.Component;

import javax.persistence.Query;
import java.util.List;

@Component
public class UserDAOImpl implements UserDAO{

    @Override
    public User findUserById(long user_id) {
        Session session = HibernateUtils.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        User user =  session.get(User.class, user_id);
        transaction.commit();
        return user;
    }

    @Override
    public User findUserByLogin(String login) {
        Session session = HibernateUtils.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        Query query =  session.createQuery("From User where login = :param",User.class);
        query.setParameter("param",login.trim().toLowerCase());
        List<User> users = query.getResultList();
        transaction.commit();
        if (users.isEmpty())
            return null;
        return users.get(0);
    }

    @Override
    public User findUserByEmail(String email) {
        Session session = HibernateUtils.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        Query query =  session.createQuery("From User where email = :param",User.class);
        query.setParameter("param",email.trim().toLowerCase());
        List<User> users = query.getResultList();
        transaction.commit();
        if (users.isEmpty())
            return null;
        return users.get(0);
    }

    @Override
    public void save(User user) {
        Session session = HibernateUtils.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        session.save(user);
        transaction.commit();
    }

    @Override
    public void merge(User user) {
        Session session = HibernateUtils.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        session.merge(user);
        transaction.commit();
    }

    @Override
    public void delete(User user) {
        Session  session = HibernateUtils.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        session.delete(user);
        transaction.commit();
    }

    @Override
    public void update(User user) {
        Session session = HibernateUtils.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        session.update(user);
        transaction.commit();
    }

    @Override
    public void evict(User user) {
        Session session = HibernateUtils.getSessionFactory().getCurrentSession();
        session.evict(user);
    }

    @Override
    public List<User> findAll() {
        Session session = HibernateUtils.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        List<User> users = session.createQuery("From User",User.class).list();
        transaction.commit();
        return  users;
    }
}
