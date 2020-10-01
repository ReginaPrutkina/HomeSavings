package project;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

public class MainFile2 {
    public static void main(String[] args) {

        //Для отпарвки с аккаута gmail потрбовалось дать разрешение на использование чужими сервисами
try {
        UserDAO userDAO = new UserDAOImpl();
        SendMail sender = null;
        UserService userService = new UserServiceImpl(userDAO);
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");


        //Счтиваем почту и пароль отправителя из базы по логину adminSender =======================
        User userSender = userDAO.findUserByLogin("adminSender");
        if (userSender == null){
            System.out.println("Логин администратора почтовых уведомлений в базе не найден");
            //Регистрация adminSender (однократно )=====================================================
            System.out.println("Регистрируем adminSender");
            User admin = new AdminSender();
            admin.setLogin("adminSender");
            admin.setRole("admin");
            admin.setEmail("rprutkina@gmail.com");
            admin.setPasswordHash("Talia0610");
            admin.setFamily("Администратор1");
            admin.setName("Админ1");
            userDAO.save(admin);
            userSender = userDAO.findUserByLogin("adminSender");
        }
      //  sender = new SendMail(userSender.getEmail(),((AdminSender)userSender).getPassword());
        //Создаем почтовый сервис через бин
        sender = (SendMail)context.getBean("MailService");
        sender.setUsername(userSender.getEmail());
        sender.setPassword(((AdminSender)userSender).getPassword());

       //Считываем список валют с сайта ЦБР
        GetCurrencyRatesCB cbrCurrencies = new CBRCurrencies();

    //Регистрация пользователя ==================================================================
  //  userService.registerUser();

    // Аутентификация ползователя ================================================================
   //User user1 = userService.userAuth();
    User user1 = userDAO.findUserByLogin("petrov");
    if ( user1 != null)
        System.out.println("Приятной работы, "+ user1.getName() + " " + user1.getFamily());
        String userLogin = user1.getLogin();

   //Используем mock - данные для создания списка депозитов ==================================
    List<Deposit> depositList = (new MockData()).depositList;
    //        for (Deposit deposit: depositList ) {
    //           user2.addDeposit(deposit);
    //        }

    //    userDAO.merge(user2);
    //userDAO.update(user2); //- не работает

   // Обогощаем список считанных из базы депозитов клиента полями - сервиса валюты и классами по расчету %% ========================
    //Создание через бины
    //DepositService depositService = new DepositService();
    DepositService depositService = (DepositService)context.getBean("DepositFactory");
//    for (Deposit deposit : user1.getDeposits()) {
//        deposit.setCurrencyRatesCB(cbrCurrencies);
//        depositService.addTypeOfPercent(deposit);
//
//    }
    // Создаем сервис нотификации бином
        NotificationService notificationService = (NotificationService) context.getBean("NotificationService");
        notificationService.setDepositList( user1.getDeposits());
        //NotificationService notificationService = new NotificationService( user1.getDeposits());
        notificationService.setDaysToEndOfDeposit(60);
        System.out.println(notificationService.getRegularText());
        System.out.println(notificationService.getWarningText());
       // String fileName = notificationService.toFile(notificationService.getRegularHTML());
        String fileName = notificationService.toFile(notificationService.getWarningHTML());

        //Отправка нотификаций  ===============================================================================
        if (sender != null){
           //   sender.send("Информирование по депозитам",notificationService.getRegularText(),"rprutkina@mail.ru");
           //   sender.send("Истекает срок депозитов",notificationService.getWarningText(),"rprutkina@mail.ru");
          //    String fileName = notificationService.toFile(notificationService.getRegularHTML());
              if (fileName.length() > 0){
              //  sender.sendFile("Информирование по депозитам",fileName,"rprutkina@mail.ru");
                }
        }

        }catch (MyException myException) {
            myException.printStackTrace();
        }
        }
}
