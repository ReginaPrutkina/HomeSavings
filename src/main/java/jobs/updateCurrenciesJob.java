package jobs;

import currencyService.*;
import myException.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import services.BDServices.CurrencyDAO;

import java.util.List;

@Service
public class updateCurrenciesJob {
    @Autowired
    private CBRCurrencies currencyRatesCB;

    @Autowired
    CurrencyFactoryService currencyFactory;

    @Autowired
    CurrencyDAO currencyDAO;

  //  @Scheduled(cron="0 0 0 * * MON-FRI")      //каждый будний день в полночь
    @Scheduled(cron="0 32 14 23 * *")
    public void newCurrencyRates() throws MyException {
        //Считываем новые курсы валют
        currencyRatesCB.setNodeCurrenciesList(currencyRatesCB.getXML());
        //обнуляем старые курсы валют в мапе currencyFactory
        currencyFactory.clearCurrencyMap();
        //Сохраняем курсы в БД
        saveCurrencyRates();
    }

    private void saveCurrencyRates() throws MyException {
        Currency currency = currencyFactory.getCurrency("840");
        // Проверяем, есть ли в базе данные за дату из нового курса
        List<Currency>  currencyList = currencyDAO.getCurrencyByCode("840", currency.getData());
        if (currencyList.isEmpty()) {
            //Сохраняем список валют в базе
            // одновременно заполняется мапа по валютам из списка currencyFactory.getCurrencyNumList()
            for (String currencyCode : currencyFactory.getCurrencyNumList()) {
                currencyDAO.save(currencyFactory.getCurrency(currencyCode));
            }
        }
    }
}
