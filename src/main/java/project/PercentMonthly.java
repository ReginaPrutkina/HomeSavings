package project;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class PercentMonthly implements TypeOfPercent {
    @Override
    public int getNumTypeOfPercent() {
        return 30; //%%  ежемесячно
    }
    @Override
    public double sumOnEndOfPeriod(Date startDate, Date endDate, double sumOfDeposit,double rateOfInterest) {
        // ежемесячная капитализация
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(startDate);
        Calendar calendarEnd = new GregorianCalendar();
        calendarEnd.setTime(endDate);
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.add(Calendar.MONTH,1);
        while (calendar.getTime().compareTo(endDate) <= 0){

             sumOfDeposit+=sumOfDeposit*rateOfInterest/100/365*daysInMonth;  //считаем год  - 365 дней
         //   System.out.println("сумма " + sumOfDeposit + " на " + calendar.getTime());
            daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            calendar.add(Calendar.MONTH,1);
           }
        return (double)Math.round(sumOfDeposit*100)/100;
    };

    @Override
    public String toString(){
        return "Ежемесячно";
    }

};

