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
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        while (calendar.getTime().before(endDate) ){
            daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            calendar.add(Calendar.MONTH,1);
            if (calendar.getTime().before(endDate) )
                sumOfDeposit+=sumOfDeposit*rateOfInterest/100/365*daysInMonth;  //считаем год  - 365 дней
           }
        return (double)Math.round(sumOfDeposit*100)/100;
    };

    @Override
    public String toString(){
        return "Ежемесячно";
    }

};

