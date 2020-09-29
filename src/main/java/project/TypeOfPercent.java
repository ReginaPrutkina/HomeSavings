package project;

import java.util.Date;

public interface TypeOfPercent {

    double sumOnEndOfPeriod(Date startDate, Date endDate, double sumOfDeposit, double rateOfInterest);

    int getNumTypeOfPercent();

    default double effectiveRate(double rateOfInterest){
        double sum=100;
        long yearMS = 365L*24L*60L*60L*1000L;
        Date start = new Date();
        Date end = new Date(start.getTime()+yearMS); //считаем год  - 365 дней
        sum = sumOnEndOfPeriod(start, end, sum,rateOfInterest);
        return (double)Math.round((sum-100)*100)/100;
    };
}
