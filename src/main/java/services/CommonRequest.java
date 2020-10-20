package services;

import dataClasses.User;
import org.springframework.stereotype.Component;

@Component
public class CommonRequest {
    private Integer sessionUID;
    private User user;

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
    }
}
