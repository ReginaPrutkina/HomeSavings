package project.techClasses;

import dataClasses.Deposit;
import dataClasses.User;
import services.BDServices.UserDAOImpl;

import java.util.Scanner;

public class TestDepositChange {
    private User user;
    private UserDAOImpl userDAO;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserDAOImpl getUserDAO() {
        return userDAO;
    }

    public void setUserDAO(UserDAOImpl userDAO) {
        this.userDAO = userDAO;
    }

    public boolean changeFromConsole (){
        int id;
        double newSum;
        Scanner scanner = new Scanner(System.in);
        System.out.println("введите номер депозита для изменения суммы");
        id = scanner.nextInt();
        System.out.println("введите сумму");
        newSum= scanner.nextDouble();
        return changeDepositSum(id,newSum);
    }

    public boolean changeDepositSum(int id_deposit, double newSum) {
        boolean isChanged = false;
        for (Deposit deposit: user.getDeposits() ) {
            if (deposit.getId() == id_deposit){
                deposit.setSum(newSum);
                isChanged = true;
            }
            if (isChanged){
                userDAO.merge(user);
                System.out.println("Данные обновлены");
            }
            else
                System.out.println("Депозит с идентификатором "+ id_deposit + "  не принадлежит клиенту + " + user.getFamily() + " " + user.getName());
        }
        return isChanged;
    }
}
