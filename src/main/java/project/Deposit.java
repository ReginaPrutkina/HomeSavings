package project;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Deposit {
    private int id;
    private String bankName;
    private double sum;
    private double rateOfInterest;
    private String currencyCode;   //810,840,978
    private Date startDate;
    private Date endDate;
    private String comment;
    private TypeOfPercent typeOfPercent;
    private GetCurrencyRatesCB CurrencyRatesCB;
    Deposit (HashMap<String,String> fieldsMap) throws MyException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        try {
            this.id = Integer.parseInt(fieldsMap.get("id"));
            this.bankName = fieldsMap.get("bankName");
            this.sum = Double.parseDouble(fieldsMap.get("sum"));
            this.rateOfInterest = Double.parseDouble(fieldsMap.get("rateOfInterest"));
            this.currencyCode = fieldsMap.get("currencyCode");
            this.startDate = formatter.parse(fieldsMap.get("startDate"));
            this.endDate = formatter.parse(fieldsMap.get("endDate"));
            this.comment = fieldsMap.get("comment");
            switch (Integer.parseInt(fieldsMap.get("percentType"))) {
                case 1-> {setTypeOfPercent(new PercentDaily());}
                case 30-> {setTypeOfPercent(new PercentMonthly());}
                default -> {setTypeOfPercent(new PercentAtTheEnd());}
            }
        }
        catch (ParseException e) {
            throw new MyException( "Ошибка формата данных для депозита сообщения", e);
        }
    }

    public Date getEndDate() {
        return endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public double getRateOfInterest() {
        return rateOfInterest;
    }

    public double getSum() {
        return sum;
    }

    public void setTypeOfPercent(TypeOfPercent typeOfPercent) {
        this.typeOfPercent = typeOfPercent;
    }

    public TypeOfPercent getTypeOfPercent() {
        return typeOfPercent;
    }

    public GetCurrencyRatesCB getCurrencyRatesCB() {
        return CurrencyRatesCB;
    }

    public void setCurrencyRatesCB(GetCurrencyRatesCB currencyRatesCB) {
        CurrencyRatesCB = currencyRatesCB;
    }

    public String header(){
        return String.format("%5s |","id") +
                String.format("%25s |","Название банка") +
                String.format("%15s |","Сумма")  +
                String.format("%5s|","Ставка")  +
                String.format("%6s |","Валюта")  +
                String.format("%14s |","Дата начала") +
                String.format("%14s |","Дата окончания") +
                String.format("%30s |","Комменатрий") +
                String.format("%10s ","Капитализация") +
                "\n";
        }
    @Override
    public String toString() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        return String.format("%5d |",id) +
                String.format("%25s |",bankName) +
                String.format("%15.2f |",sum)  +
                String.format("%5.2f |",rateOfInterest)  +
                // Здесь надо заменить на буквенный код валюты из Currency
                String.format("%6s |   ",currencyCode)  +
                formatter.format(startDate) +
                "  | " + formatter.format(endDate) +
                String.format("    |%30s |",comment) +
                typeOfPercent ;
    }
}
