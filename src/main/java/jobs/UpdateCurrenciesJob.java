package jobs;

import currencyService.*;
import log.Logging;
import myException.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import services.BDServices.CurrencyDAO;

import java.util.List;

@Service
public class UpdateCurrenciesJob {
    @Autowired
    private CBRCurrencies currencyRatesCB;

    @Autowired
    CurrencyFactoryService currencyFactory;

    @Autowired
    CurrencyDAO currencyDAO;

    @Autowired
    Logging logging;

    // Варианты cron:
    //  @Scheduled(cron="0 0 0 * * MON-FRI")      //каждый будний день в полночь
    //  @Scheduled(cron="0 45 17 23 * *")        // 23 числа каждого месяца в 17:45:00
    @Scheduled(cron = "${cronUpdateCurrencyRates}")
    public void newCurrencyRates()  {
        logging.log(" Запуск задачи по расписанию.");
        try {
            //Считываем новые курсы валют
            currencyRatesCB.setNodeCurrenciesList(currencyRatesCB.getXML());
            //обнуляем старые курсы валют в мапе currencyFactory
            currencyFactory.clearCurrencyMap();
            //Сохраняем курсы в БД
            saveCurrencyRates();
        } catch (MyException e) {
            logging.log(" Задача завершена с ошибкой.", e);
        }
        logging.log(" Задача завершена.");
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

    public CBRCurrencies getCurrencyRatesCB() {
        return currencyRatesCB;
    }

    public void setCurrencyRatesCB(CBRCurrencies currencyRatesCB) {
        this.currencyRatesCB = currencyRatesCB;
    }

    public CurrencyFactoryService getCurrencyFactory() {
        return currencyFactory;
    }

    public void setCurrencyFactory(CurrencyFactoryService currencyFactory) {
        this.currencyFactory = currencyFactory;
    }

    public CurrencyDAO getCurrencyDAO() {
        return currencyDAO;
    }

    public void setCurrencyDAO(CurrencyDAO currencyDAO) {
        this.currencyDAO = currencyDAO;
    }

    public Logging getLogging() {
        return logging;
    }

    public void setLogging(Logging logging) {
        this.logging = logging;
    }
}
