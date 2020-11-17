package notification;

import homeSavingsException.HomeSavingsException;

public interface AllUserNotification {
    void sendNotification(boolean isRegular) throws HomeSavingsException;
}
