package project;

import java.util.List;

public class MainFile1 {
    public static void main(String[] args) {
          try {
        UserDAO userDAO = new UserDAOImpl();
        UserService userService = new UserServiceImpl(userDAO);
        // User user2 = userService.registerUser();
        //  User user2 = userService.userAuth();
        User user2 = userDAO.findUserByLogin("shvartz1");
         /*   User user2 = new User();
            user2.setFamily("Шварц6");
            user2.setName("Евгений");
            user2.setLogin("shvartz6");
            user2.setEmail("shvartz6@shvartz.com");
            user2.setPasswordHash("shvartz6");
            user2.setRole("user");
          */

        //  userDAO.save(user2);
        if (user2 != null) {
            System.out.println("Приятной работы, " + user2.getName() + " " + user2.getFamily());

          // userDAO.delete(user2);
            //Используем mock - данные для создания списка депозитов
                  List<Deposit> depositList = (new MockData()).depositList;
   //        for (Deposit deposit: depositList ) {
    //           user2.addDeposit(deposit);
   //        }

    //    userDAO.merge(user2);
        //userDAO.update(user2);

     //      System.out.println(user2);
            boolean isHeaderPrint = false;
           for (Deposit deposit: userDAO.findUserByLogin("shvartz1").getDeposits() ) {
               if (!isHeaderPrint)
               {System.out.println(deposit.header());
               isHeaderPrint = true;}

               System.out.println(deposit);

           }
    }
        }catch (MyException myException) {
            myException.printStackTrace();
        }
    }
}
