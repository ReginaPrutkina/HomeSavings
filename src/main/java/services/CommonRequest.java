package services;

import dataClasses.User;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class CommonRequest {
    private Integer sessionUID;
    private User user;
    private Date lastUpdate;

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

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public void clear(){
        setSessionUID(0);
        setUser(null);
        setLastUpdate(null);
    }
}
