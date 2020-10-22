package notification;

import myException.MyException;

public interface NotificationHTML {
    String getRegularHTML() throws MyException;
    String getWarningHTML() throws MyException;
}
