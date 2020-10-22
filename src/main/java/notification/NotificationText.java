package notification;

import myException.MyException;

public interface NotificationText {
    String getRegularText() throws MyException;
    String getWarningText() throws MyException;
}
