package project;

import java.util.List;

interface UserDAO {
    User findUserById(long user_id);

    User findUserByLogin(String login);

    User findUserByEmail(String login);

    void save(User user);

    void delete(User user);

    void update(User user);

    void merge(User user);

    void evict(User user);

    List<User> findAll();

}
