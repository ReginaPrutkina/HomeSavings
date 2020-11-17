package currencyService;

import homeSavingsException.HomeSavingsException;

public interface GettingCurrency {
   Currency getCurrency(String currencyNumCode) throws HomeSavingsException;
}
