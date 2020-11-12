package jobs;

import log.Logging;
import myException.MyException;
import notification.AllUserNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class NotificationJobs {
    @Autowired
    private AllUserNotification allUserNotification;

    @Autowired
    private Logging logging;

    // Примеры расписаний
    // @Scheduled(cron="0 */1 * * * ?") // запуск каждую минуту
    //  @Scheduled(cron="0 0 0 1 * *")      //каждое 1-ое число в полночь
    // @Scheduled(cron="0 0 0 * * WED")    //запуск каждую среду в полночь

    @Scheduled(cron = "${cronRegularNotification}")
    public void informJob()  {
        logging.log(" Запуск задачи по расписанию.");
        try {
            allUserNotification.sendNotification(true);
        } catch (MyException e) {
          logging.log(" Задача завершена с ошибкой.", e);
        }
        logging.log(" Задача завершена.");
    }

    @Scheduled(cron = "${cronWarningNotification}")
    public void warningJob() {
        logging.log(" Запуск задачи по расписанию.");
        try {
            allUserNotification.sendNotification(false);
        } catch (MyException e) {
            logging.log(" Задача завершена с ошибкой.", e);
        }
        logging.log(" Задача завершена.");
    }
}
