package currencyService;

import myException.MyException;
import org.w3c.dom.NodeList;

import java.util.Date;

public interface GetCurrencyRatesCB {
    Date getRatesDate();
    NodeList getNodeCurrenciesList();
    void setNodeCurrenciesList(NodeList nodeCurrenciesList);
    NodeList getXML() throws MyException;
}
