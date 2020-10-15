package notification;

import myException.MyException;

public interface AllUserNotification {
    public void sendNotification(boolean isRegular) throws MyException;
}
