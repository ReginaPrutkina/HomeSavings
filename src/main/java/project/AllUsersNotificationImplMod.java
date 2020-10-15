package project;
import log.Logging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import module1.SendMailService;

import javax.mail.MessagingException;
import java.util.Objects;

public class AllUsersNotificationImplMod implements AllUserNotification {

    private String adminLogin;

    private String fileName;

    @Value(".\\userFiles\\")
    private String notificationDir;

    @Autowired
    private SendMailService sender;

    @Autowired
    private UserDAOImpl userDAO;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private Logging logging;

    AllUsersNotificationImplMod(String adminLogin) {
        this.adminLogin = adminLogin;
    }

    public String getAdminLogin() {
        return adminLogin;
    }

    public void setAdminLogin(String adminLogin) {
        this.adminLogin = adminLogin;
    }

    public SendMailService getSender() {
        return sender;
    }

    public void setSender(SendMailService sender) {
        this.sender = sender;
    }

    public UserDAOImpl getUserDAO() {
        return userDAO;
    }

    public void setUserDAO(UserDAOImpl userDAO) {
        this.userDAO = userDAO;
    }

    public NotificationService getNotificationService() {
        return notificationService;
    }

    public void setNotificationService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * @param isRegular true - информирование о текущем статусе,
     *                  false - информирование об истекших и итекающих по сроку депозитах
     * @throws MyException пробрасывает исключение MessagingException
     */
    @Override
    public void sendNotification(boolean isRegular) throws MyException{
        User userSender = userDAO.findUserByLogin(this.adminLogin);
        String fileName;
        if (userSender == null) {
            System.out.println("Логин администратора почтовых уведомлений в базе не найден");
            throw new MyException("Логин администратора почтовых уведомлений в базе не найден: " + adminLogin);
        }
        logging.setUserName(adminLogin);
        logging.log("Начинаем рассылку уведомлений клиентам. Администратор: " + adminLogin);
        sender.setUsername(userSender.getEmail());
        sender.setPassword(((AdminSender) userSender).getPassword());

        for (User user : userDAO.findAll()) {
            if (user.getRole().equals("admin")) continue;
            notificationService.setDepositList(user.getDeposits());
            if (isRegular)
                fileName = notificationService.toFile(notificationService.getRegularHTML(),createUserFileName(user.getLogin()));
            else
                fileName = notificationService.toFile(notificationService.getWarningHTML(),createUserFileName(user.getLogin()));

                if (fileName.length() > 0) {
                    try {
                        sender.sendFile("Информирование по депозитам", fileName, user.getEmail());
                        logging.log("Отправлено информирование по депозитам клиенту "
                                + user.getFamily() + " " + user.getName()
                                + " по адресу: " + user.getEmail()
                                );

                    }catch (MessagingException e)
                    { throw new MyException( "Ошибка отправки сообщения", e);
                    }
                }
        }
        logging.setUserName("");
    }

    private String createUserFileName (String userLogin){
        return Objects.requireNonNullElse(notificationDir, ".\\") + userLogin + "_info.html";
    }
}
