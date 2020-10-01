package project;

import java.util.List;

public class MainFile {
    public static void main(String[] args) {

        //Для отпарвки с аккаута gmail потрбовалось дать разрешение на использование чужими сервисами
try {
        UserDAO userDAO = new UserDAOImpl();
        SendMail sender = null;
        //Ввод почты и пароля отправителя для уведомлений вручную
/*
        Scanner sc = new Scanner(System.in);
        String sender_mail = "rprutkina@gmail.com";
        System.out.print("введите пароль для "+sender_mail+": ");
        String user_pass = sc.nextLine();
        SendMail sender = new SendMail(sender_mail,user_pass);
*/
        //Счтиваем почту и пароль отправителя из базы по логину adminSender
        User userSender = userDAO.findUserByLogin("adminSender");
        if (userSender != null)
            sender = new SendMail(userSender.getEmail(),((AdminSender)userSender).getPassword());
        else
            System.out.println("Логин администратора почтовых уведомлений в базе не найден");

    //Используем mock - данные для создания списка депозитов
        List<Deposit> depositList = (new MockData()).depositList;

        //Считываем список валют с сайта ЦБР
        GetCurrencyRatesCB cbrCurrencies = new CBRCurrencies();


        //попробоать распечатать список валют через getCurrency
        System.out.println("Дата курсов валют: " + cbrCurrencies.getRatesDate());
        System.out.println(cbrCurrencies.getCurrency("840"));
        System.out.println(cbrCurrencies.getCurrency("810"));
        System.out.println(cbrCurrencies.getCurrency("944"));
        //
        NotificationService notificationService = new NotificationService(depositList);
        System.out.println(notificationService.getRegularText());
        System.out.println(notificationService.getWarningText());
   /*
        //Отправка нотификаций
        if (sender != null){
              sender.send("Информирование по депозитам",notificationService.getRegularText(),"rprutkina@mail.ru");
              sender.send("Истекает срок депозитов",notificationService.getWarningText(),"rprutkina@mail.ru");
              String fileName = notificationService.toFile(notificationService.getRegularText());
              if (fileName.length() > 0){
                sender.sendFile("Информирование по депозитам",fileName,"rprutkina@mail.ru");
                }
        }
*/
    UserService userService = new UserServiceImpl(userDAO);
    //Регистрация adminSender1
/*
    User admin = new AdminSender();
    admin.setRole("admin");
    admin.setEmail("rprutkina@mail.ru");
    admin.setLogin("adminSender1");
    admin.setPasswordHash("Talia0610");
    admin.setFamily("Администратор1");
    admin.setName("Админ1");
    userDAO.save((User)admin);
*/
        //Регистрация пользователя

        userService.registerUser();
        User user1 = userService.userAuth();
        if ( user1 != null)
            System.out.println("Приятной работы, "+ user1.getName() + " " + user1.getFamily());
         User user2 = userService.userAuth();
         if ( user2 != null)
            System.out.println("Приятной работы, "+ user2.getName() + " " + user2.getFamily());

        }catch (MyException myException) {
            myException.printStackTrace();
        }
        }
}
