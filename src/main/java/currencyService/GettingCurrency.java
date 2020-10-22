package currencyService;

import myException.MyException;

public interface GettingCurrency {
   Currency getCurrency(String currencyNumCode) throws MyException;
}
