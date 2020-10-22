package currencyService;

import myException.MyException;
import org.w3c.dom.NodeList;

import java.util.Date;

public interface GetCurrencyRatesCB {
    public Date getRatesDate();
    public NodeList getNodeCurrenciesList();
    public void setNodeCurrenciesList(NodeList nodeCurrenciesList);
    public NodeList getXML() throws MyException;
}
