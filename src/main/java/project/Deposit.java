package project;

import javax.persistence.*;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
@Entity
@Table (name = "deposits")
public class Deposit implements Serializable {
    @Id
    @Column
    @GeneratedValue
    private int id;
    @Column
    private String bankName;
    @Column
    private double sum;
    @Column
    private double rateOfInterest;
    @Column
    private String currencyCode;   //810,840,978...
    @Column
    @Temporal(TemporalType.DATE)
    private Date startDate;
    @Temporal(TemporalType.DATE)
    private Date endDate;
    @Column
    private String comment;
    @Column
    private int percentType;
    @Transient
    private TypeOfPercent typeOfPercent;
    @Transient
    private GetCurrencyRatesCB CurrencyRatesCB;

    @ManyToOne
    @JoinColumn (name = "user_id")
    private User user;

    Deposit(){}

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
            this.percentType = Integer.parseInt(fieldsMap.get("percentType"));
            switch (this.percentType) {
                case 1-> {setTypeOfPercent(new PercentDaily());}
                case 30-> {setTypeOfPercent(new PercentMonthly());}
                default -> {setTypeOfPercent(new PercentAtTheEnd());}
            }
        }
        catch (ParseException e) {
            throw new MyException( "Ошибка формата данных для депозита ", e);
        }
    }


    public void setUser(User user) {
        this.user = user;
    }
    public User getUser() {
        return this.user;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public double getRateOfInterest() {
        return rateOfInterest;
    }

    public void setRateOfInterest(double rateOfInterest) {
        this.rateOfInterest = rateOfInterest;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getPercentType() {
        return percentType;
    }

    public void setPercentType(int percentType) {
        this.percentType = percentType;
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
