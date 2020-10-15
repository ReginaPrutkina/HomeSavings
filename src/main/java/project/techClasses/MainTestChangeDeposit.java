package project.techClasses;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import project.NotificationService;
import project.User;
import project.UserDAOImpl;

public class MainTestChangeDeposit {
    public static void main(String[] args) {

    ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
    UserDAOImpl userDAO = (UserDAOImpl) context.getBean("UserDAO") ;
    User user = userDAO.findUserByLogin("petrov");
    TestDepositChange testDepositChange = new TestDepositChange();
    NotificationService notificationService = (NotificationService) context.getBean("NotificationService");
    notificationService.setDepositList (user.getDeposits());
    System.out.println(notificationService.getRegularText());
    testDepositChange.setUser(user);
    testDepositChange.setUserDAO(userDAO);
    testDepositChange.changeFromConsole();
    user = userDAO.findUserByLogin("petrov");
    testDepositChange.setUser(user);
    notificationService.setDepositList(user.getDeposits());
    System.out.println(notificationService.getRegularText());

    }
}
