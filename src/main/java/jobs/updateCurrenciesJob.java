package jobs;

import currencyService.CBRCurrencies;
import currencyService.CurrencyFactory;
import currencyService.CurrencyFactoryService;
import currencyService.GetCurrencyRatesCB;
import myException.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class updateCurrenciesJob {
    @Autowired
    private CBRCurrencies currencyRatesCB;

    @Autowired
    CurrencyFactoryService currencyFactory;

  //  @Scheduled(cron="0 0 0 * * MON-FRI")      //каждый будний день в полночь
    @Scheduled(cron="0 46 16 22 * *")
    public void newCurrencyRates() throws MyException {
        //Считываем новые курсы валют
        currencyRatesCB.setNodeCurrenciesList(currencyRatesCB.getXML());
        //обнуляем старые курсы валют в мапе currencyFactory
        currencyFactory.clearCurrencyMap();
    }
}
