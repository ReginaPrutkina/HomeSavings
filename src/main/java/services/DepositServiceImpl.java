package services;

import businessLogicClasses.PercentTypeFactory;
import businessLogicClasses.TypeOfPercent;
import dataClasses.Deposit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepositServiceImpl implements  DepositService {

    @Autowired
    PercentTypeFactory percentTypeFactory;

    public PercentTypeFactory getPercentTypeFactory() {
        return percentTypeFactory;
    }

    public void setPercentTypeFactory(PercentTypeFactory percentTypeFactory) {
        this.percentTypeFactory = percentTypeFactory;
    }

    @Override
    public TypeOfPercent getTypeOfPercentObject(Deposit deposit) {
        return percentTypeFactory.getTypeOfPercentMap().get(deposit.getPercentType());
    }
    @Override
    public double getEffectiveRate (Deposit deposit) {
        return getTypeOfPercentObject(deposit).effectiveRate(deposit.getRateOfInterest());
    }

    @Override
    public double getSumOnEndOfPeriod (Deposit deposit){
        return getTypeOfPercentObject(deposit).sumOnEndOfPeriod(deposit.getStartDate(),deposit.getEndDate(),deposit.getSum(),deposit.getRateOfInterest());
    }
}
