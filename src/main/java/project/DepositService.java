package project;

import java.util.List;

public class DepositService implements DepositServiceActions{
    //здесь д б объект связи в ресурсом хранения данных - БД или файл
    NeedToNotify notification;
    public DepositService(NeedToNotify notification){
        this.notification = notification;
    }
    @Override
    public List<Deposit> getAllDeposits() {
        return null;
    }

    @Override
    public Deposit getDeposit(int Id) {
        return null;
    }

    @Override
    public boolean addDeposit(Deposit deposit) {
        return false;
    }

    @Override
    public boolean updateDeposit(Deposit deposit) {
        return false;
    }

    @Override
    public boolean deleteDeposit(int ID) {
        return false;
    }

   }
