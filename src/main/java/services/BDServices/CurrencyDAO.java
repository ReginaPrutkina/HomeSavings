package services.BDServices;

import currencyService.Currency;

import java.util.Date;
import java.util.List;

public interface CurrencyDAO {
    void save(Currency currency);
    void update(Currency currency);
    void delete(Currency currency);
    List<Currency> getCurrencyByCode(String currencyNumCode);
    List<Currency> getCurrencyByCode(String currencyNumCode, Date data);
    List<Currency> getCurrencyByCode(String currencyNumCode, Date startDate, Date endDate);
}
