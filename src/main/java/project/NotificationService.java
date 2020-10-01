package project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.*;

public class NotificationService implements NotificationText, NotificationHTML {

    private String notificationText;
    private boolean notifyFlag;
    private List<Deposit> depositList;

    @Value("30")  // по умолчанию отправляем предупреждение об окончании срока депозита в течениен 30 дней
    private int daysToEndOfDeposit ;

    @Autowired
    GetCurrencyRatesCB currencyRatesCB;

    @Autowired
    DepositService depositService;

    NotificationService() {}

    NotificationService(List<Deposit> depositList){

        this.depositList = depositList;
    }

    public GetCurrencyRatesCB getCurrencyRatesCB() {
        return currencyRatesCB;
    }

    public void setCurrencyRatesCB(GetCurrencyRatesCB currencyRatesCB) {
        this.currencyRatesCB = currencyRatesCB;
    }

    public DepositService getDepositService() {
        return depositService;
    }

    public void setDepositService(DepositService depositService) {
        this.depositService = depositService;
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
        StringBuilder tempStr = new StringBuilder();
        if (depositList.isEmpty())
            return "";
        Date date = new Date();
        int rowNum = 1;
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        tempStr.append("Состояние вкладов на " + formatter.format(date) + "\n");
        tempStr.append(depositTextHeader());
        Set<String> currencySet = new HashSet<>();
        for (Deposit deposit: depositList) {
            tempStr.append(this.depositTextRow(rowNum++, deposit) +'\n');
            currencySet.add(deposit.getCurrencyCode());
        }
        tempStr.append(footerText(currencySet,"\n"));
        notificationText = tempStr.toString();
        return notificationText;
    }

    private String footerText(Set<String> currencySet, String endStr){
        StringBuilder tempStr = new StringBuilder();
        tempStr.append("Всего: "+ endStr);
        for (String curCode: currencySet) {
            double sumInCurrency = 0;

            for (Deposit deposit: depositList) {
                if (deposit.getCurrencyCode().equals(curCode))
                    sumInCurrency += deposit.getSum();
            }
            if (curCode.equals("810"))
                tempStr.append("Вкладов в рублях: " + sumInCurrency + endStr);
            else {
                Currency currency = this.currencyRatesCB.getCurrency(curCode);
                tempStr.append("Вкладов в " + currency.getCharCode() + " (" + currency.getName() + "): " );
                tempStr.append( sumInCurrency + ", рублевый экв.: " );
                tempStr.append(sumInCurrency * currency.getValue() / currency.getNominal());
                tempStr.append("  (по курсу ЦБ - " + currency.getValue()  + " руб. за " );
                tempStr.append(currency.getNominal() + " " + currency.getCharCode() + ")" +endStr);
            }
        }

        return tempStr.toString();
    }
    @Override
    public String getWarningText(){
        String overEndDeposits="";
        String nearEndDeposit="";
        if (depositList.isEmpty())
            return "";
        Date date = new Date();
        int rowNumNearEnd = 1;
        int rowNumOverEnd = 1;
        for (Deposit deposit: depositList) {
            if (deposit.getEndDate().getTime() < date.getTime())
                overEndDeposits += depositTextRow(rowNumOverEnd++,deposit) + '\n';
            else if (deposit.getEndDate().getTime() < (date.getTime() + (long)this.daysToEndOfDeposit * 24 * 60 * 60 * 1000))
                nearEndDeposit += depositTextRow(rowNumNearEnd++, deposit) + "\n";
        }

            if (overEndDeposits.length() > 0) {
                notificationText = "Вклады, срок которых закончился: \n" +
                        depositTextHeader() +
                        overEndDeposits +
                        "\n";
            }
            if (nearEndDeposit.length() > 0) {
                notificationText += "Вклады, срок которых закончится в ближайшие " +
                        this.daysToEndOfDeposit + " дней: \n" +
                        depositTextHeader() +
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
        htmlStringBuilder.append("<table id = \"info\">");
        //thead row
        htmlStringBuilder.append(depositHTMLHeader());
        //append rows
        int rowNum = 1;
        for (Deposit deposit: depositList) {
            htmlStringBuilder.append(depositHTMLRow(rowNum,deposit));
            currencySet.add(deposit.getCurrencyCode());
        }
        htmlStringBuilder.append("</table><p>"+footerText(currencySet, "<br>")+"</p>");
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
        //thead rows
        overEndDeposits.append("<table id = \"info\">");
        overEndDeposits.append(depositHTMLHeader());
        nearEndDeposit.append("<table id = \"info\">");
        nearEndDeposit.append(depositHTMLHeader());

        //append rows to tables
        int rowNumNearEnd = 1;
        int rowNumOverEnd = 1;
        for (Deposit deposit: depositList) {
            if (deposit.getEndDate().getTime() < date.getTime()){
                overEndDeposits.append(depositHTMLRow(rowNumOverEnd++, deposit));
            }
            else if (deposit.getEndDate().getTime() < (date.getTime() + (long)this.daysToEndOfDeposit * 24 * 60 * 60 * 1000)){
                nearEndDeposit.append(depositHTMLRow(rowNumNearEnd++,deposit));
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
            htmlStringBuilder.append(nearEndDeposit);
        }
        htmlStringBuilder.append("</body></html>");
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

     private String depositTextRow(int rowNum, Deposit deposit){
         SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
         String currencyString;
         if (deposit.getCurrencyCode().equals("810"))
             currencyString = "RUB";
         else
         if (this.currencyRatesCB == null )
             currencyString = deposit.getCurrencyCode();
         else
             currencyString = this.currencyRatesCB.getCurrency(deposit.getCurrencyCode()).getCharCode();
         return String.format("%5d |",rowNum) +
                 String.format("%25s |", deposit.getBankName()) +
                 String.format("%15.2f |",deposit.getSum())  +
                 String.format("%5.2f |",deposit.getRateOfInterest())  +
                 String.format("%6s |   ",currencyString)  +
                 formatter.format(deposit.getStartDate()) +
                 "  | " + formatter.format(deposit.getEndDate()) +
                 String.format("    |%15s |",depositService.getTypeOfPercentObject(deposit)) +
                 String.format("%30s ",deposit.getComment()) ;

     }
    private String depositTextHeader(){
        return String.format("%5s |","id") +
                String.format("%25s |","Название банка") +
                String.format("%15s |","Сумма")  +
                String.format("%5s|","Ставка")  +
                String.format("%6s |","Валюта")  +
                String.format("%14s |","Дата начала") +
                String.format("%14s |","Дата окончания") +
                String.format("%15s |","Капитализация") +
                String.format("%30s ","Комменатрий") +
                "\n";
    }

    private String depositHTMLHeader(){
        return "<thead><tr>" +
                "<th><b>#</b></th>" +
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
    }

    private String depositHTMLRow(int rowNum, Deposit deposit){
        String currencyString;
        if (deposit.getCurrencyCode().equals("810"))
            currencyString = "RUB";
        else
        if (this.currencyRatesCB == null )
            currencyString = deposit.getCurrencyCode();
        else
            currencyString = currencyRatesCB.getCurrency(deposit.getCurrencyCode()).getCharCode();

        return "<tr><td>"+ rowNum +"</td>" +
                "<td>" +deposit.getBankName()+ "</td>" +
                "<td>"+deposit.getSum()+"</td>"+
                "<td>" +deposit.getRateOfInterest() +" %</td>" +
                "<td>" + currencyString +"</td>"+
                "<td>" + deposit.getStartDate() +"</td>"+
                "<td>"+deposit.getEndDate()+"</td>" +
                "<td>"+deposit.getComment()+"</td>"  +
                "<td>"+depositService.getTypeOfPercentObject(deposit) + "</td>"+
                "<td>"+depositService.getTypeOfPercentObject(deposit).effectiveRate(deposit.getRateOfInterest()) + "%</td>" +
                "<td>"+depositService.getTypeOfPercentObject(deposit).sumOnEndOfPeriod(deposit.getStartDate(),deposit.getEndDate(),deposit.getSum(),deposit.getRateOfInterest())
                + "</td></tr>";
    }

}
