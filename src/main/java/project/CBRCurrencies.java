package project;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedInputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CBRCurrencies implements GetCurrencyRatesCB{
    private String url = "http://cbr.ru/scripts/XML_daily.asp";
    //private List<Currency> currenciesList;
    private NodeList nodeCurrenciesList;
    private Date ratesDate;

    CBRCurrencies() throws MyException {
        //считываем файл из url
        this.nodeCurrenciesList =  getXML();
       }

    public NodeList getXML() throws MyException {
        //считываем файл из url
        NodeList nList = null;
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        try {
            BufferedInputStream fXmlFile = new BufferedInputStream(new URL(url).openStream());
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            ratesDate = formatter.parse(doc.getDocumentElement().getAttribute("Date"));
            nList = doc.getElementsByTagName("Valute");

        } catch (Exception e) {
            throw new MyException("Не удалось считать курсы ЦБ с сайта " + url, e);

        }
        return nList;
    }

    @Override
    public Currency getCurrency(String currencyNumCode){
        //получение валюты по коду
        for (int temp = 0; temp < this.nodeCurrenciesList.getLength(); temp++) {
            Node nNode = this.nodeCurrenciesList.item(temp);
            Element eElement = (Element) nNode;
            try{
                if ((eElement.getElementsByTagName("NumCode").item(0).getTextContent()).equals(currencyNumCode))
                return new Currency(
                        eElement.getAttribute("ID"),
                        eElement.getElementsByTagName("NumCode").item(0).getTextContent(),
                        eElement.getElementsByTagName("CharCode").item(0).getTextContent(),
                        Integer.parseInt(eElement.getElementsByTagName("Nominal").item(0).getTextContent()),
                        eElement.getElementsByTagName("Name").item(0).getTextContent(),
                        Double.valueOf(eElement.getElementsByTagName("Value").item(0).getTextContent().replace(",",".")));
        } catch (NumberFormatException ex){
                ex.printStackTrace();
            }
        }
        return null;
    }
    @Override
    public Date getRatesDate(){
        return ratesDate;
    }
}
