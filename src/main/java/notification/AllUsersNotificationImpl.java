package notification;
import log.Logging;
import homeSavingsException.HomeSavingsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import dataClasses.AdminSender;
import mailService.SendMail;
import dataClasses.User;
import services.BDServices.UserDAO;
import java.util.Objects;

public class AllUsersNotificationImpl implements AllUserNotification {

    private String adminLogin;

    @Value(".\\userFiles\\")
    private String notificationDir;

    @Autowired
    private SendMail sender;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private Logging logging;

    AllUsersNotificationImpl(String adminLogin) {
        this.adminLogin = adminLogin;
    }

    public String getAdminLogin() {
        return adminLogin;
    }

    public void setAdminLogin(String adminLogin) {
        this.adminLogin = adminLogin;
    }

    public SendMail getSender() {
        return sender;
    }

    public void setSender(SendMail sender) {
        this.sender = sender;
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }

    public void setUserDAO(UserDAO userDAO) {
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
     * @throws HomeSavingsException пробрасывает исключение MessagingException
     */
    @Override
    public void sendNotification(boolean isRegular) throws HomeSavingsException {
        User userSender = userDAO.findUserByLogin(this.adminLogin);
        if (userSender == null) {
            System.out.println("Логин администратора почтовых уведомлений в базе не найден");
            throw new HomeSavingsException("Логин администратора почтовых уведомлений в базе не найден: " + adminLogin);
        }
        logging.setUserName(adminLogin);
        logging.log("Начинаем рассылку уведомлений клиентам. Администратор: " + adminLogin);
        sender.setUsername(userSender.getEmail());
        sender.setPassword(((AdminSender) userSender).getPassword());

        for (User user : userDAO.findAll()) {
            String fileName;
            if (user.getRole().equals("admin")) continue;
            notificationService.setDepositList(user.getDeposits());
            if (isRegular)
                fileName = notificationService.toFile(notificationService.getRegularHTML(),createUserFileName(user.getLogin()));
            else
                fileName = notificationService.toFile(notificationService.getWarningHTML(),createUserFileName(user.getLogin()));

                if (fileName.length() > 0) {
                        sender.sendFile("Информирование по депозитам", fileName, user.getEmail());
                        logging.log("Отправлено информирование по депозитам клиенту "
                                + user.getFamily() + " " + user.getName()
                                + " по адресу: " + user.getEmail()
                                );
                }
        }
        logging.setUserName("");
    }

    private String createUserFileName (String userLogin){
        return Objects.requireNonNullElse(notificationDir, ".\\") + userLogin + "_info.html";
    }
}
