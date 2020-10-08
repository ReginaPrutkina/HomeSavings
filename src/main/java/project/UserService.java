package project;

public interface UserService {

    boolean isUserValid(User user);

    User checkUserPassword(String login, String password);

    User userAuth(String login, String password);

    User registerUser(User user);
}
