package dataClasses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import myException.MyException;

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

    @Column (nullable = false)
    private double sum;

    @Column (nullable = false)
    private double rateOfInterest;

    @Column (nullable = false)
    private String currencyCode;   //810,840,978...

    @Column (nullable = false)
    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Column (nullable = false)
    @Temporal(TemporalType.DATE)
    private Date endDate;

    @Column
    private String comment;

    @Column (nullable = false)
    private int percentType;

    @JsonIgnore   // решает проблему зацикливания реста
    @ManyToOne
    @JoinColumn (name = "user_id")
    private User user;

    Deposit(){}

    public Deposit(HashMap<String, String> fieldsMap) throws MyException {
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
        }
        catch (ParseException e) {
            throw new MyException( "Ошибка формата данных для депозита ", e);
        }
    }

    public int getId() {
        return id;
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

    @Override
    public String toString() {
        return "Deposit{" +
                "id=" + id +
                ", bankName='" + bankName + '\'' +
                ", sum=" + sum +
                ", rateOfInterest=" + rateOfInterest +
                ", currencyCode='" + currencyCode + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", comment='" + comment + '\'' +
                ", percentType=" + percentType +
                '}';
    }
}
