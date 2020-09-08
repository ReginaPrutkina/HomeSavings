package project;

import java.util.Date;

public interface TypeOfPercent {
    double sumOnEndOfPeriod(Date startDate, Date endDate, double sumOfDeposit, double rateOfInterest);
    int getNumTypeOfPercent();
    default double effectiveRate(double rateOfInterest){
        double sum=1;
        Date start = new Date();
        Date end = new Date(start.getTime()+365*24*60*60*1000); //считаем год  - 365 дней
        sum = sumOnEndOfPeriod(start, end, sum,rateOfInterest);
        return Math.round((sum-1)*10000)/100; // округлениe до d.dd
    };
}
