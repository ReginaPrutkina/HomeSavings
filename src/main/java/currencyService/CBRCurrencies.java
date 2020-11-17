package currencyService;

import homeSavingsException.HomeSavingsException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedInputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CBRCurrencies implements GetCurrencyRatesCB{
    // url = "http://cbr.ru/scripts/XML_daily.asp"  в xml - конфигурации
    private String url;

    private NodeList nodeCurrenciesList;

    private Date ratesDate;

    //Параметр конструктора url задается в xml -конфигурации SPRING
    CBRCurrencies(String url) throws HomeSavingsException {
        //считываем файл из url
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
    public NodeList getXML() throws HomeSavingsException {
        //считываем файл из url
        NodeList nList;
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
            throw new HomeSavingsException("Не удалось считать курсы ЦБ с сайта " + url, e);

        }
        return nList;
    }

    @Override
    public Date getRatesDate(){
        return ratesDate;
    }
}
