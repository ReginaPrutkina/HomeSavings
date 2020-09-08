package project;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.*;

public class NotificationService implements NeedToNotify{
    private String notificationText;
    private boolean notifyFlag;
    private List<Deposit> depositList;
    private int dayForNotification = 1; // по умолчанию отправляем регулярное сообщение 1-го числа каждого месяца
    private int daysToEndOfDeposit = 30; // по умолчанию отправляем предупреждение об окончании срока депозита в течениен 30 дней
    NotificationService(List<Deposit> depositList){
        this.depositList = depositList;
        if (isDateToNotify())
        {
            notificationText = getRegularText();
            notifyFlag = true;
        }
        if (isNeedToWarn())
        {
            notificationText += "Истекает срок вклада";
            notifyFlag = true;
        }

    }

    public void setDayForNotification(int dayForNotification){
        this.dayForNotification = dayForNotification;
    }
    public void setDaysToEndOfDeposit(int daysToEndOfDeposit){

        this.daysToEndOfDeposit = daysToEndOfDeposit;
    }

    public String getRegularText(){
        if (depositList.isEmpty())
            return "";
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        notificationText = "Состояние вкладов на " + formatter.format(date) + "\n";
        notificationText += depositList.get(0).header();
        Set<String> currencySet = new HashSet<>();
        for (Deposit deposit: depositList) {
            notificationText += deposit.toString() +'\n';
            currencySet.add(deposit.getCurrencyCode());
        }
        notificationText += footerText(currencySet);
        return notificationText;
    };
    private String footerText(Set<String> currencySet){
        String footer = "Всего: \n";
        for (String curCode: currencySet) {
            double sumInCurrency = 0;

            for (Deposit deposit: depositList) {
                if (deposit.getCurrencyCode().equals(curCode))
                    sumInCurrency += deposit.getSum();
            }
            if (curCode.equals("810"))
                footer += "Вкладов в рублях: " + sumInCurrency +
                        "\n";
            else {
                Currency currency = depositList.get(0).getCurrencyRatesCB().getCurrency(curCode);
                footer += "Вкладов в " + currency.getCharCode() +
                " (" + currency.getName() + "): " +
                        sumInCurrency +
                ", рублевый экв.: " +
                        sumInCurrency * currency.getValue() / currency.getNominal() +
                        "  (по курсу ЦБ - " + currency.getValue()  + " руб. за " +
                        currency.getNominal() + " " + currency.getCharCode() +
                ")\n";
            }
        }

        return footer;
    }

    public String getWarningText(){
        String overEndDeposits="";
        String nearEndDeposit="";
        if (depositList.isEmpty())
            return "";
        Date date = new Date();

        for (Deposit deposit: depositList) {
            if (deposit.getEndDate().getTime() < date.getTime())
                overEndDeposits += deposit.toString() + '\n';
            else if (deposit.getEndDate().getTime() < (date.getTime() + (long)this.daysToEndOfDeposit * 24 * 60 * 60 * 1000))
                nearEndDeposit += deposit.toString() + "\n";
        }

            if (overEndDeposits.length() > 0) {
                notificationText = "Вклады, срок которых закончился: \n" +
                        depositList.get(0).header() +
                        overEndDeposits +
                        "\n";
            }
            if (nearEndDeposit.length() > 0) {
                notificationText += "Вклады, срок которых закончится в ближайшие " +
                        this.daysToEndOfDeposit + " дней: \n" +
                        depositList.get(0).header() +
                        nearEndDeposit +
                        "\n";
            }
        return notificationText;
        };

    public boolean getNotifyFlag(){
        return notifyFlag;
    }

    @Override
    public boolean isNeedToWarn() {
        return false;
    }

    @Override
    public boolean isDateToNotify() {
        return false;
    }
     public String toFile(String notificationText){
        String fileName = ".\\" + "info.txt";
        Path infoFile = Paths.get(fileName);
        try {
            Files.write(infoFile, Collections.singleton(notificationText),StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        } catch ( IOException exception) {
            fileName = "";
            exception.printStackTrace();
        }

        return fileName;
     }


}
