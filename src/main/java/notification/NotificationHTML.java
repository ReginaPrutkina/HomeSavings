package notification;

import homeSavingsException.HomeSavingsException;

public interface NotificationHTML {
    String getRegularHTML() throws HomeSavingsException;
    String getWarningHTML() throws HomeSavingsException;
}
