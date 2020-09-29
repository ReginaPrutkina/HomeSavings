package project;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.*;

public class NotificationService implements Notification {
    private String notificationText;
    private boolean notifyFlag;
    private List<Deposit> depositList;
    //@Value("30")  // по умолчанию отправляем предупреждение об окончании срока депозита в течениен 30 дней
    private int daysToEndOfDeposit ;

    NotificationService() {};

    NotificationService(List<Deposit> depositList){
        this.depositList = depositList;
    }

    public String getNotificationText() {
        return notificationText;
    }

    public void setNotificationText(String notificationText) {
        this.notificationText = notificationText;
    }

    public boolean isNotifyFlag() {
        return notifyFlag;
    }

    public void setNotifyFlag(boolean notifyFlag) {
        this.notifyFlag = notifyFlag;
    }

    public List<Deposit> getDepositList() {
        return depositList;
    }

    public void setDepositList(List<Deposit> depositList) {
        this.depositList = depositList;
    }

    public int getDaysToEndOfDeposit() {
        return daysToEndOfDeposit;
    }

    public void setDaysToEndOfDeposit(int daysToEndOfDeposit){

        this.daysToEndOfDeposit = daysToEndOfDeposit;
    }
    @Override
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
    }
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
    @Override
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
        }

    public boolean getNotifyFlag(){
        return notifyFlag;
    }

    private String htmlStyle(){
        return "<style>" +
                "#info th {" +
                "padding: 10px;" +
                "background: lightblue;" +
                "}" +
                "#info td {" +
                "padding: 10px;" +
                "background: gainsboro;" +
                "}" +
                ".footnote {"+
                    "color:gray;" +
                    "font: 12px/14px;" +
                "}" +
                "</style>";
    }

    @Override
    public String getRegularHTML() {
        if (depositList.isEmpty())
            return "";
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        Set<String> currencySet = new HashSet<>();
        String currencyString;
        //define a HTML String Builder
        StringBuilder htmlStringBuilder=new StringBuilder();
        //append html header and title
        String title = "Состояние вкладов на " + formatter.format(date);
        htmlStringBuilder.append("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"><title>"+ title +"</title></head>");
        //append style
        htmlStringBuilder.append(htmlStyle());
        //append body
        htmlStringBuilder.append("<body>");
        htmlStringBuilder.append("<h3>"+ "Уважаемый " + depositList.get(0).getUser().getName() +"! </h3>");
        htmlStringBuilder.append("<h3>"+ "Состояние Ваших вкладов на " + formatter.format(date) +":</h3>");
        //append table
        //htmlStringBuilder.append("<table border=\"1\" bordercolor=\"#000000\">");
         htmlStringBuilder.append("<table id = \"info\">");
        //thead row
        htmlStringBuilder.append("<thead><th><b>#</b></th>" +
                        "<th><b>Название банка</b></th>" +
                        "<th><b>Сумма</b></th>"+
                        "<th><b>Ставка</b></th>" +
                        "<th><b>Валюта</b></th>"+
                        "<th><b>Дата начала</b></th>"+
                        "<th><b>Дата окончания</b></th>" +
                        "<th><b>Комменатрий</b></th>"  +
                        "<th><b>Капитализация</b></th>" +
                        "<th><b>Эфф.ставка</b></th>" +
                        "<th><b>*Сумма на конец срока</b></th>" +
                        "</tr></thead>"
                        );
        //append rows
        int rowNum = 1;
        for (Deposit deposit: depositList) {
            if (deposit.getCurrencyCode().equals("810"))
                currencyString = "RUB";
            else
            if (deposit.getCurrencyRatesCB() == null )
                currencyString = deposit.getCurrencyCode();
            else
                currencyString = deposit.getCurrencyRatesCB().getCurrency(deposit.getCurrencyCode()).getCharCode();

            htmlStringBuilder.append("<tr><td>"+ rowNum++ +"</td>" +
                    "<td>" +deposit.getBankName()+ "</td>" +
                    "<td>"+deposit.getSum()+"</td>"+
                    "<td>" +deposit.getRateOfInterest() +" %</td>" +
                    "<td>" + currencyString +"</td>"+
                    "<td>" + deposit.getStartDate() +"</td>"+
                    "<td>"+deposit.getEndDate()+"</th>" +
                    "<td>"+deposit.getComment()+"</td>"  +
                    "<td>"+deposit.getTypeOfPercent() + "</td>"+
                    "<td>"+deposit.getTypeOfPercent().effectiveRate(deposit.getRateOfInterest()) + "%</td>" +
                    "<td>"+deposit.getTypeOfPercent().sumOnEndOfPeriod(deposit.getStartDate(),deposit.getEndDate(),deposit.getSum(),deposit.getRateOfInterest()) + "</td>"
            );
            currencySet.add(deposit.getCurrencyCode());
        }
        String htmlFooter =  footerText(currencySet).replace("\n","<br>");
        htmlStringBuilder.append("</table><p>"+htmlFooter+"</p>");
        htmlStringBuilder.append("<p class = \"footnote\">"+ "* Суммы на конец срока рассчитаны без учета снятий и пополнений" +"</p></body></html>");
        notificationText = htmlStringBuilder.toString();
        return notificationText;
        }

    @Override
    public String getWarningHTML() {
        String tempStr;
        if (depositList.isEmpty())
            return "";
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        String currencyString;
        //define a HTML String Builders
        StringBuilder htmlStringBuilder=new StringBuilder();
        StringBuilder overEndDeposits=new StringBuilder();
        StringBuilder nearEndDeposit=new StringBuilder();
        //append html header and title
        String title = "Состояние вкладов на " + formatter.format(date);
        htmlStringBuilder.append("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"><title>"+ title +"</title></head>");
        //append style
        htmlStringBuilder.append(htmlStyle());
        //append body
        htmlStringBuilder.append("<body>");
        htmlStringBuilder.append("<h3>"+ "Уважаемый " + depositList.get(0).getUser().getName() +"! </h3>");
        htmlStringBuilder.append("<h3>"+ "Вклады, требующие Вашего внимания: " + formatter.format(date) +":</h3>");
        //append  2 tables
        //thead row
        tempStr = "<thead><th><b>#</b></th>" +
                "<th><b>Название банка</b></th>" +
                "<th><b>Сумма</b></th>"+
                "<th><b>Ставка</b></th>" +
                "<th><b>Валюта</b></th>"+
                "<th><b>Дата начала</b></th>"+
                "<th><b>Дата окончания</b></th>" +
                "<th><b>Комменатрий</b></th>"  +
                "<th><b>Капитализация</b></th>" +
                "<th><b>Эфф.ставка</b></th>" +
                "<th><b>*Сумма на конец срока</b></th>" +
                "</tr></thead>";
        overEndDeposits.append("<table id = \"info\">");
        overEndDeposits.append(tempStr);
        nearEndDeposit.append("<table id = \"info\">");
        nearEndDeposit.append(tempStr);

        //append rows
        int rowNumNearEnd = 1;
        int rowNumOverEnd = 1;
        for (Deposit deposit: depositList) {
            if (deposit.getCurrencyCode().equals("810"))
                currencyString = "RUB";
            else
            if (deposit.getCurrencyRatesCB() == null )
                currencyString = deposit.getCurrencyCode();
            else
                currencyString = deposit.getCurrencyRatesCB().getCurrency(deposit.getCurrencyCode()).getCharCode();
            tempStr =
                    "<td>" +deposit.getBankName()+ "</td>" +
                    "<td>"+deposit.getSum()+"</td>"+
                    "<td>" +deposit.getRateOfInterest() +" %</td>" +
                    "<td>" + currencyString +"</td>"+
                    "<td>" + deposit.getStartDate() +"</td>"+
                    "<td>"+deposit.getEndDate()+"</th>" +
                    "<td>"+deposit.getComment()+"</td>"  +
                    "<td>"+deposit.getTypeOfPercent() + "</td>"+
                    "<td>"+deposit.getTypeOfPercent().effectiveRate(deposit.getRateOfInterest()) + "%</td>" +
                    "<td>"+deposit.getTypeOfPercent().sumOnEndOfPeriod(deposit.getStartDate(),deposit.getEndDate(),deposit.getSum(),deposit.getRateOfInterest()) +
                    "</td>";

            if (deposit.getEndDate().getTime() < date.getTime()){
                overEndDeposits.append("<tr><td>"+ rowNumOverEnd++ +"</td>" + tempStr);
            }
            else if (deposit.getEndDate().getTime() < (date.getTime() + (long)this.daysToEndOfDeposit * 24 * 60 * 60 * 1000)){
                nearEndDeposit.append("<tr><td>"+ rowNumNearEnd++ +"</td>" + tempStr);
            }

        }

        if (rowNumOverEnd > 1) {
            overEndDeposits.append("</table>");
            htmlStringBuilder.append("<p>Вклады, срок которых закончился: </p>");
            htmlStringBuilder.append(overEndDeposits);

        }
        if (rowNumNearEnd > 1) {
            nearEndDeposit.append("</table>");
            htmlStringBuilder.append("<p>Вклады, срок которых закончится в ближайшие " + this.daysToEndOfDeposit +" дней:  </p>");
            htmlStringBuilder.append(overEndDeposits);
        }
        htmlStringBuilder.append("</body><html>");
        if (rowNumNearEnd >1 || rowNumOverEnd > 1)
            this.notificationText = htmlStringBuilder.toString();
        else this.notificationText = "";
        return notificationText;
    }

    public String toFile(String notificationText) throws MyException{
        String fileName = ".\\" + "info.html";
        Path infoFile = Paths.get(fileName);
        try {
            Files.write(infoFile, Collections.singleton(notificationText),StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        } catch ( IOException exception) {
            fileName = "";
            throw (new MyException(" Не удалось создать файл с информированием по депозитам ", exception));
        }

        return fileName;
     }


}
