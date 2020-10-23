package currencyService;

import java.util.List;

public interface CurrencyFactoryService extends GettingCurrency{
    void clearCurrencyMap();
    List<String> getCurrencyNumList();

}
