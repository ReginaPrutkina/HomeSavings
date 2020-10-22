package currencyService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import myException.MyException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedInputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

//@Component
public class CBRCurrencies implements GetCurrencyRatesCB{

   private String url;      // = "http://cbr.ru/scripts/XML_daily.asp";

    private NodeList nodeCurrenciesList;

    private Date ratesDate;

        CBRCurrencies(String url) throws MyException {
        //считываем файл из url
            System.out.println("новый экземпляр CBRCurrencies");
            this.url = url;
        this.nodeCurrenciesList =  getXML();
    }

    @Override
    public NodeList getNodeCurrenciesList() {
        return nodeCurrenciesList;
    }

    @Override
    public void setNodeCurrenciesList(NodeList nodeCurrenciesList) {
        this.nodeCurrenciesList = nodeCurrenciesList;
    }

    @Override
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
    public Date getRatesDate(){
        return ratesDate;
    }
}
