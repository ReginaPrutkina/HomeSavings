package project;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class test {
    public static void main(String[] args) {
        try {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = (Date) sdf.parse("2020-12-30");
        Date date2 = null;

            date2 = (Date) sdf.parse("2021-12-30");
            System.out.println("");

        PercentMonthly percentMonthly = new PercentMonthly();
            System.out.println(percentMonthly);
        System.out.println(percentMonthly.sumOnEndOfPeriod(date1,date2,20000,10.7));
            System.out.println(percentMonthly.effectiveRate(10.7));
            PercentDaily percentDaily = new PercentDaily();
            System.out.println(percentDaily);
            System.out.println(percentDaily.sumOnEndOfPeriod(date1,date2,20000,10.7));
            System.out.println(percentDaily.effectiveRate(10.7));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
