package project;

import java.util.List;
import java.util.Scanner;

public class MainFile {
    public static void main(String[] args) {
        //Для отпарвки с аккаута gmail потрбовалось дать разрешение на использование чужими сервисами
try {   Scanner sc = new Scanner(System.in);
        String sender_mail = "rprutkina@gmail.com";
        System.out.print("введите пароль для "+sender_mail+": ");
        String user_pass = sc.nextLine();
        SendMail sender = new SendMail(sender_mail,user_pass);

        //Используем mock - данные для создания списка депозитов
        List<Deposit> depositList = (new MockData()).depositList;

        //Считываем список валют с сайта ЦБР
        GetCurrencyRatesCB cbrCurrencies = new CBRCurrencies();
        //System.out.print(depositList.get(0).header());
        for (Deposit deposit:depositList){
           // System.out.println(deposit);
            deposit.setCurrencyRatesCB(cbrCurrencies);
        }

        //попробоать распечатать список валют через getCurrency
        System.out.println("Дата курсов валют: " + cbrCurrencies.getRatesDate());
        System.out.println(cbrCurrencies.getCurrency("840"));
        System.out.println(cbrCurrencies.getCurrency("810"));
        System.out.println(cbrCurrencies.getCurrency("944"));

       NotificationService notificationService = new NotificationService(depositList);
        System.out.println(notificationService.getRegularText());
        System.out.println(notificationService.getWarningText());
/*
        sender.send("Информирование по депозитам",notificationService.getRegularText(),"rprutkina@mail.ru");

        sender.send("Истекает срок депозитов",notificationService.getWarningText(),"rprutkina@mail.ru");
        String fileName = notificationService.toFile(notificationService.getRegularText());
        if (fileName.length() > 0){
                sender.sendFile("Информирование по депозитам",fileName,"rprutkina@mail.ru");
        }

*/
        //Регистрация пользователя
        UserDAO userDAO = new UserDAOImpl();
        UserService userService = new UserServiceImpl(userDAO);
        userService.registerUser();
        User user = userService.userAuth();
        if ( user != null)
            System.out.println("Приятной работы, "+ user.getName() + " " + user.getFamily());

        }catch (MyException myException) {
            myException.printStackTrace();
        }
        }
}
