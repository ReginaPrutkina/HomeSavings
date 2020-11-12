package services;

import businessLogicClasses.TypeOfPercent;
import dataClasses.Deposit;
import myException.MyException;

public interface DepositService {

    TypeOfPercent getTypeOfPercentObject(Deposit deposit);

    double getEffectiveRate (Deposit deposit);

    double getSumOnEndOfPeriod (Deposit deposit);

    boolean isDepositValid (Deposit deposit);
}
