package project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MockData {
    public List<Deposit> depositList = new ArrayList<>();
    public MockData() throws MyException{
        //Mock данные для списка вкладов
        HashMap<String, String> hashMapDeposit = new HashMap<>();
        hashMapDeposit.put("id", "1");
        hashMapDeposit.put("bankName", "Сбербанк");
        hashMapDeposit.put("owner", "Джон");
        hashMapDeposit.put("sum", "100000.00");
        hashMapDeposit.put("rateOfInterest", "5.2");
        hashMapDeposit.put("currencyCode", "810");
        hashMapDeposit.put("startDate", "03.04.2020");
        hashMapDeposit.put("endDate", "03.09.2020");
        hashMapDeposit.put("comment", "еждневная капитализация");
        hashMapDeposit.put("percentType", "1");
        depositList.add(new Deposit(hashMapDeposit));
        depositList.get(depositList.size() - 1).setTypeOfPercent(new PercentDaily());

        hashMapDeposit.clear();
        hashMapDeposit.put("id", "2");
        hashMapDeposit.put("bankName", "Сбербанк");
        hashMapDeposit.put("owner", "Джон");
        hashMapDeposit.put("sum", "200100.00");
        hashMapDeposit.put("rateOfInterest", "6.5");
        hashMapDeposit.put("currencyCode", "810");
        hashMapDeposit.put("startDate", "03.04.2020");
        hashMapDeposit.put("endDate", "03.11.2020");
        hashMapDeposit.put("comment", "не капитализируется, доверенность на Джину");
        hashMapDeposit.put("percentType", "0");
        depositList.add(new Deposit(hashMapDeposit));
        depositList.get(depositList.size() - 1).setTypeOfPercent(new PercentAtTheEnd());

        hashMapDeposit.clear();
        hashMapDeposit.put("id", "3");
        hashMapDeposit.put("bankName", "ВТБ");
        hashMapDeposit.put("owner", "Джина");
        hashMapDeposit.put("sum", "20000.00");
        hashMapDeposit.put("rateOfInterest", "3.0");
        hashMapDeposit.put("currencyCode", "840");
        hashMapDeposit.put("startDate", "25.12.2019");
        hashMapDeposit.put("endDate", "25.12.2020");
        hashMapDeposit.put("comment", "ежемесячная капитализация");
        hashMapDeposit.put("percentType", "30");
        depositList.add(new Deposit(hashMapDeposit));
        depositList.get(depositList.size() - 1).setTypeOfPercent(new PercentMonthly());

        hashMapDeposit.clear();
        hashMapDeposit.put("id", "3");
        hashMapDeposit.put("bankName", "Открытие");
        hashMapDeposit.put("owner", "Джина");
        hashMapDeposit.put("sum", "12000.00");
        hashMapDeposit.put("rateOfInterest", "2.3");
        hashMapDeposit.put("currencyCode", "840");
        hashMapDeposit.put("startDate", "25.02.2020");
        hashMapDeposit.put("endDate", "25.09.2020");
        hashMapDeposit.put("comment", "ежемесячная капитализация");
        hashMapDeposit.put("percentType", "30");
        depositList.add(new Deposit(hashMapDeposit));
        depositList.get(depositList.size() - 1).setTypeOfPercent(new PercentMonthly());

    }
}
