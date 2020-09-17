package project;

public interface UserService {
    boolean isUserValid(User user, UserDAO userDAO);
    boolean checkUserPassword(User user, UserDAO userDAO);
}
