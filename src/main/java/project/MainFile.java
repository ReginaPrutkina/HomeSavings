package project;
//import module.LogToFile;
import dataClasses.User;
import myException.MyException;
import notification.AllUsersNotificationImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import services.UserServiceImpl;

public class MainFile {
    public static void main(String[] args) {

        //Для отпарвки с аккаута gmail потрбовалось дать разрешение на использование чужими сервисами
        try {
            ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
            UserServiceImpl userService = (UserServiceImpl) context.getBean("UserService");
            AllUsersNotificationImpl allUsersNotification = (AllUsersNotificationImpl) context.getBean("AllUserNotificationService");
        //    AllUsersNotificationImpl allUsersNotificationImplMod = (AllUsersNotificationImpl) context.getBean("AllUserNotificationServiceMod");
        //    LogToFile logToFile = (LogToFile)context.getBean("LogService");



            //Счтиваем почту и пароль отправителя из базы по логину adminSender =======================
//            User userSender = userDAO.findUserByLogin("adminSender");
//            if (userSender == null){
//                System.out.println("Логин администратора почтовых уведомлений в базе не найден");
//                //Регистрация adminSender (однократно )=====================================================
//                System.out.println("Регистрируем adminSender");
//                User admin = new AdminSender();
//                admin.setLogin("adminSender");
//                admin.setRole("admin");
//                admin.setEmail("rprutkina@gmail.com");
//                admin.setPasswordHash("Talia0610");
//                admin.setFamily("Администратор1");
//                admin.setName("Админ1");
//                userDAO.save(admin);
//                userSender = userDAO.findUserByLogin("adminSender");
//            }

//            //Регистрация пользователя ==================================================================
 //             userService.consoleRegisterUser();
//
//            // Аутентификация ползователя ================================================================
                User user1 = userService.consoleUserAuth();
//           // User user1 = userDAO.findUserByLogin("petrov");
 //           if ( user1 != null)
 //               System.out.println("Приятной работы, "+ user1.getName() + " " + user1.getFamily());
//            String userLogin = user1.getLogin();
//            UserDAOImpl userDAO = (UserDAOImpl) context.getBean("UserDAO") ;
//
//
//            //Используем mock - данные для создания списка депозитов ==================================
//                      List<Deposit> depositList = (new MockData()).depositList;
//                    for (Deposit deposit: depositList ) {
//                       user1.addDeposit(deposit);
//                    }
//
 //           userService.getUserDAO().merge(user1);
            //userDAO.update(user2); //- не работает

            //DepositService depositService = (DepositService)context.getBean("DepositFactory");

            // Создаем сервис нотификации бином
           // NotificationService notificationService = (NotificationService) context.getBean("NotificationService");

//            notificationService.setDepositList( user1.getDeposits());
//            notificationService.setDaysToEndOfDeposit(60);
//            System.out.println(notificationService.getRegularText());
//            System.out.println(notificationService.getWarningText());
//            String fileName = notificationService.toFile(notificationService.getRegularHTML());
//            //  String fileName = notificationService.toFile(notificationService.getWarningHTML());

            //Отправка нотификаций  ===============================================================================
            //if (sender != null){
                //   sender.send("Информирование по депозитам",notificationService.getRegularText(),"rprutkina@mail.ru");
                //   sender.send("Истекает срок депозитов",notificationService.getWarningText(),"rprutkina@mail.ru");
                //    String fileName = notificationService.toFile(notificationService.getRegularHTML());
             //   if (fileName.length() > 0){
                    //  sender.sendFile("Информирование по депозитам",fileName,"rprutkina@mail.ru");
             //  }
          //  }

            allUsersNotification.sendNotification(true);


        }catch (MyException myException) {
            myException.printStackTrace();
        }
    }
}
