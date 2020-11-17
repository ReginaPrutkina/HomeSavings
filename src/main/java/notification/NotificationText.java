package notification;

import homeSavingsException.HomeSavingsException;

public interface NotificationText {
    String getRegularText() throws HomeSavingsException;
    String getWarningText() throws HomeSavingsException;
}
