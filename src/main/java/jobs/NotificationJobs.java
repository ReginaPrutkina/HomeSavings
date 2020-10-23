package jobs;

import myException.MyException;
import notification.AllUserNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class NotificationJobs {
    @Autowired
    private AllUserNotification allUserNotification;

   // @Scheduled(cron="0 */1 * * * ?") // запуск каждую минуту
  //  @Scheduled(cron="0 0 0 1 * *")      //каждое 1-ое число в полночь
    @Scheduled(cron="0 40 15 22 * *")      //каждое 22-ое число в 15:40
    public void informJob() throws MyException {
        allUserNotification.sendNotification(true);
    }

    // @Scheduled(cron="0 */1 * * * ?") // запуск каждые 3 минуты
   // @Scheduled(cron="0 0 0 * * WED")    //запуск каждую среду в полночь
    @Scheduled(cron="0 05 17 * * THU")    //запуск каждую среду в 16:15
    public void warningJob() throws MyException {
        allUserNotification.sendNotification(false);
    }
}
