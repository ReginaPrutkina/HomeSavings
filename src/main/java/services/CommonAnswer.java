package services;

import dataClasses.User;
import org.springframework.stereotype.Component;

@Component
public class CommonAnswer {
    private Integer sessionUID;
    private User user;
    private String errorText;

    public String getErrorText() {
        return errorText;
    }

    public void setErrorText(String errorText) {
        this.errorText = errorText;
    }

    public Integer getSessionUID() {
        return sessionUID;
    }

    public void setSessionUID(Integer sessionUID) {
        this.sessionUID = sessionUID;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void clear(){
        setSessionUID(0);
        setUser(null);
        setErrorText(null);
    }
}
