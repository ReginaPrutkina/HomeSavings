package businessLogicClasses;

import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class PercentDaily implements TypeOfPercent {
    @Override
    public int getNumTypeOfPercent() {
        return 1;}
        //%%  ежедневно

    @Override
    public double sumOnEndOfPeriod(Date startDate, Date endDate, double sumOfDeposit,double rateOfInterest) {
        int daysInPeriod=(int)((endDate.getTime()-startDate.getTime())/(24*60*60*1000));
        for(int day=1; day<=daysInPeriod; day++)
            sumOfDeposit+=sumOfDeposit*rateOfInterest/100/365;  //считаем год  - 365 дней
        return (double)Math.round(sumOfDeposit*100)/100;
    }

    @Override
    public String toString(){
        return "Ежедневно";
    }

}
