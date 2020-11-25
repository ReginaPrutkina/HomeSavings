package services;

import businessLogicClasses.PercentTypeFactory;
import businessLogicClasses.TypeOfPercent;
import currencyService.CurrencyFactory;
import dataClasses.Deposit;
import log.Logging;
import homeSavingsException.HomeSavingsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepositServiceImpl implements  DepositService {

    @Autowired
    PercentTypeFactory percentTypeFactory;

    @Autowired
    CurrencyFactory currencyFactory;

    @Autowired
    Logging logging;

    public PercentTypeFactory getPercentTypeFactory() {
        return percentTypeFactory;
    }

    public void setPercentTypeFactory(PercentTypeFactory percentTypeFactory) {
        this.percentTypeFactory = percentTypeFactory;
    }

    @Override
    public TypeOfPercent getTypeOfPercentObject(Deposit deposit) {
        return percentTypeFactory.getTypeOfPercentMap().get(deposit.getPercentType());
    }
    @Override
    public double getEffectiveRate (Deposit deposit) {
        return getTypeOfPercentObject(deposit).effectiveRate(deposit.getRateOfInterest());
    }

    @Override
    public double getSumOnEndOfPeriod (Deposit deposit){
        return getTypeOfPercentObject(deposit).sumOnEndOfPeriod(deposit.getStartDate(),deposit.getEndDate(),deposit.getSum(),deposit.getRateOfInterest());
    }

    // Проверяем наличие обязательныйх полей (notNull)
    @Override
    public boolean isDepositValid(Deposit deposit) {
        return !(deposit.getBankName() == null ||
                currencyCodeNotValid(deposit.getCurrencyCode()) ||
                deposit.getEndDate() == null ||
                deposit.getStartDate() == null ||
                deposit.getEndDate().before(deposit.getStartDate()) ||
                deposit.getSum() <= 0 ||
                deposit.getRateOfInterest() < 0);
    }

    /**
     * Проверяем, что  нет такой валюты в списке и это не рубли
     * @param currencyCode - числовой код валюты
     * @return  false, если валюта рубли или есть в списке валют ЦБ,
     *          true -  есле нет в списке валют или ошибка парсинга
     */
    private boolean currencyCodeNotValid(String currencyCode) {
        try {
            return ((currencyCode == null) ||
                    (!currencyCode.equals("810") && (currencyFactory.getCurrency(currencyCode) == null)));
        } catch (HomeSavingsException e) {
            logging.log("Возникло ислючение при парсинге валюты с кодом "+ currencyCode, e);
        return true;
        }
    }
}
