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
        hashMapDeposit.put("sum", "100600.00");
        hashMapDeposit.put("rateOfInterest", "4.2");
        hashMapDeposit.put("currencyCode", "810");
        hashMapDeposit.put("startDate", "06.07.2020");
        hashMapDeposit.put("endDate", "06.10.2020");
        hashMapDeposit.put("comment", "еждневная капитализация");
        hashMapDeposit.put("percentType", "1");
        depositList.add(new Deposit(hashMapDeposit));

        hashMapDeposit.clear();
        hashMapDeposit.put("id", "2");
        hashMapDeposit.put("bankName", "КредитБанк");
        hashMapDeposit.put("sum", "20010.00");
        hashMapDeposit.put("rateOfInterest", "2.7");
        hashMapDeposit.put("currencyCode", "978");
        hashMapDeposit.put("startDate", "03.05.2019");
        hashMapDeposit.put("endDate", "13.11.2020");
        hashMapDeposit.put("comment", "не капитализируется, доверенность на Павла");
        hashMapDeposit.put("percentType", "0");
        depositList.add(new Deposit(hashMapDeposit));

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

    }
}
