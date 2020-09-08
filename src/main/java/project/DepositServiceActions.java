package project;

import java.util.List;

public interface DepositServiceActions {
    List<Deposit> getAllDeposits();
    Deposit getDeposit(int Id);
    boolean addDeposit(Deposit deposit);
    boolean deleteDeposit(int Id);
    boolean updateDeposit(Deposit deposit);
}
