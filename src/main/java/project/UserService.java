package project;

public interface UserService {

    boolean isUserValid(User user);

    User checkUserPassword(String login, String password);

    User userAuth();

    User registerUser();
}
