package notification;

import myException.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class NotificationJobs {
    @Autowired
    private AllUserNotification allUserNotification;

   // @Scheduled(cron="0 */1 * * * ?") // запуск каждую минуту
  //  @Scheduled(cron="0 0 0 1 * *")      //каждое 1-ое число в полночь
    @Scheduled(cron="0 20 16 21 * *")      //каждое 1-ое число в полночь
    public void informJob() throws MyException {
        allUserNotification.sendNotification(true);
    }

    // @Scheduled(cron="0 */1 * * * ?") // запуск каждые 3 минуты
   // @Scheduled(cron="0 0 0 * * WED")    //запуск каждую среду в полночь
    @Scheduled(cron="0 15 16 * * WED")    //запуск каждую среду в полночь
    public void warningJob() throws MyException {
        allUserNotification.sendNotification(false);
    }
}
