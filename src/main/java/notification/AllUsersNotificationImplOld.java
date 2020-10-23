package notification;
import dataClasses.AdminSender;
import dataClasses.User;
import mailService.SendMail;
import myException.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import services.BDServices.UserDAOImpl;

public class AllUsersNotificationImplOld implements AllUserNotification {

    private String adminLogin;

    private String fileName;

    @Value(".\\userFiles\\")
    private String notificationDir;

    @Autowired
    private SendMail sender;

    @Autowired
    private UserDAOImpl userDAO;

    @Autowired
    private NotificationService notificationService;

    AllUsersNotificationImplOld(String adminLogin) {
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
     * @throws MyException
     */
    @Override
    public void sendNotification(boolean isRegular) throws MyException{
        User userSender = userDAO.findUserByLogin("adminSender");
        String fileName;
        if (userSender == null) {
            System.out.println("Логин администратора почтовых уведомлений в базе не найден");
            return;
        }
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
                    sender.sendFile("Информирование по депозитам", fileName, user.getEmail());
                }

        }
    }

    private String createUserFileName (String userLogin){
        if (notificationDir != null)
            return notificationDir + userLogin+"_info.html";
        return ".\\" + userLogin+"_info.html";
    }
}
