package project.techClasses;

import currencyService.Currency;
import currencyService.CurrencyFactoryService;
import homeSavingsException.HomeSavingsException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import services.BDServices.CurrencyDAOImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class CurrencyTest {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        CurrencyFactoryService currencyFactory = (CurrencyFactoryService) context.getBean("CurrencyFactory");
        CurrencyDAOImpl currencyDAO = (CurrencyDAOImpl) context.getBean("CurrencyDAO");
        try {
            //Удаляем все
            for (String currencyCode : currencyFactory.getCurrencyNumList()){
                for ( Currency cur: currencyDAO.getCurrencyByCode(currencyCode)) {
                    currencyDAO.delete(cur);

                }
            }

            // Сохраняем по списку частых валют  из currencyFactory
            for (String currencyCode : currencyFactory.getCurrencyNumList()) {
                  currencyDAO.save(currencyFactory.getCurrency(currencyCode));
            }
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = format.parse("2020-10-23");
            Date endDate = new Date();

//            for (String currencyCode : currencyFactory.getCurrencyNumList()){
//                for ( Currency cur: currencyDAO.getCurrencyByCode(currencyCode,startDate,endDate)) {
////                   for ( Currency cur: currencyDAO.getCurrencyByCode(currencyCode)) {
//                     if (cur.getNumCode().equals("840") ) currencyDAO.delete(cur);
//
//            }
//            }
            for (String currencyCode : currencyFactory.getCurrencyNumList()){
                for ( Currency cur: currencyDAO.getCurrencyByCode(currencyCode,startDate)) {
//                   for ( Currency cur: currencyDAO.getCurrencyByCode(currencyCode)) {

                    System.out.println(currencyCode);
                    System.out.println(cur);
                }
            }
            }catch (ParseException | HomeSavingsException ex){
            ex.printStackTrace();
        }
    }
}
