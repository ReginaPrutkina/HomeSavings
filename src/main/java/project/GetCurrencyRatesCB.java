package project;

import java.util.Date;

public interface GetCurrencyRatesCB {
    public Currency getCurrency(String currencyCode);
    public Date getRatesDate();
}
