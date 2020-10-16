package services;

import businessLogicClasses.PercentTypeFactory;
import businessLogicClasses.TypeOfPercent;
import dataClasses.Deposit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepositService {

    @Autowired
    PercentTypeFactory percentTypeFactory;

    public PercentTypeFactory getPercentTypeFactory() {
        return percentTypeFactory;
    }

    public void setPercentTypeFactory(PercentTypeFactory percentTypeFactory) {
        this.percentTypeFactory = percentTypeFactory;
    }

    public TypeOfPercent getTypeOfPercentObject(Deposit deposit) {
//        switch (deposit.getPercentType()) {
//            case 1 -> {return percentDaily;   }
//            case 30 -> {return percentMonthly;}
//            default -> { return percentAtTheEnd; }
//        }
        return percentTypeFactory.getTypeOfPercentMap().get(deposit.getPercentType());
    }

    public double getEffectiveRate (Deposit deposit) {
        return getTypeOfPercentObject(deposit).effectiveRate(deposit.getRateOfInterest());
    }

    public double getSumOnEndOfPeriod (Deposit deposit){
        return getTypeOfPercentObject(deposit).sumOnEndOfPeriod(deposit.getStartDate(),deposit.getEndDate(),deposit.getSum(),deposit.getRateOfInterest());
    }
}
