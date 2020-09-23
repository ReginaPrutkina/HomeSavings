package project;

import java.util.Date;

public class PercentAtTheEnd implements TypeOfPercent{
    @Override
    public int getNumTypeOfPercent() {
        return 0; //%%  в конце периода
    }
    @Override
    public double sumOnEndOfPeriod(Date startDate, Date endDate, double sumOfDeposit, double rateOfInterest) {
        int daysInPeriod=(int)((endDate.getTime()-startDate.getTime())/(24*60*60*1000));
        sumOfDeposit+=sumOfDeposit*rateOfInterest/100/365*daysInPeriod;  //считаем год  - 365 дней
        return sumOfDeposit;
    }

    @Override
    public String toString(){
        return "В конце срока";
    }
}
