package currencyService;

import myException.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CurrencyFactory implements GettingCurrency, CurrencyFactoryService {
    @Autowired
    GetCurrencyRatesCB currencyRatesCB;

    private Map<String, Currency> currencyMap;

    //Список часто исползуемых валют. Их же и сохраняем м БД
    // Задается через Spring в applicationContext.xml как аргумент конструктора
    private List<String> currencyNumList;

    CurrencyFactory(List<String> currencyNumList) {
        this.currencyNumList = currencyNumList;
        this.currencyMap = new HashMap<>();
    }

    @Override
    public Currency getCurrency(String currencyNumCode) throws MyException{
        Currency currency;
        //  если валюты нет в еще мапе, пробуем добавить ее в мап
        if (!currencyMap.containsKey(currencyNumCode)) {
            currency = parseCurrency(currencyNumCode);
            if (currency != null) {
                currency.setData(currencyRatesCB.getRatesDate());
                currencyMap.put(currencyNumCode, currency);
            }
        }
            return currencyMap.get(currencyNumCode);
    }

    //получение валюты по коду  из NodeCurrenciesList
    private Currency parseCurrency(String currencyNumCode) throws MyException{
        for (int temp = 0; temp < currencyRatesCB.getNodeCurrenciesList().getLength(); temp++) {
            Node nNode = currencyRatesCB.getNodeCurrenciesList().item(temp);
            Element eElement = (Element) nNode;
            try{
                if ((eElement.getElementsByTagName("NumCode").item(0).getTextContent()).equals(currencyNumCode))
                    return new Currency(
                            eElement.getAttribute("ID"),
                            eElement.getElementsByTagName("NumCode").item(0).getTextContent(),
                            eElement.getElementsByTagName("CharCode").item(0).getTextContent(),
                            Integer.parseInt(eElement.getElementsByTagName("Nominal").item(0).getTextContent()),
                            eElement.getElementsByTagName("Name").item(0).getTextContent(),
                            Double.parseDouble(eElement.getElementsByTagName("Value").item(0).getTextContent().replace(",",".")));
            } catch (NumberFormatException ex){
               throw new MyException("Ошибка получения валюты из XML парсинга", ex);
            }
        }
        return null;
    }

    @Override
    public List<String> getCurrencyNumList() {
        return currencyNumList;
    }

    @Override
    public void clearCurrencyMap() {
        this.currencyMap.clear();
    }

    public GetCurrencyRatesCB getCurrencyRatesCB() {
        return currencyRatesCB;
    }

    public void setCurrencyRatesCB(GetCurrencyRatesCB currencyRatesCB) {
        this.currencyRatesCB = currencyRatesCB;
    }


}
