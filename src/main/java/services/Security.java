package services;

import dataClasses.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class Security {
    private Map<Integer, User> UIDMap;
    @Value("10000")
    private int maxRandom;

    public boolean containsUID(Integer sessionUID){
        return UIDMap.containsKey(sessionUID);
    }

    public Security(){
        UIDMap = new HashMap<>();
    }

    public Integer generateAndAddUID(User user){
        Integer sessionUID;
        //генерим UID  и проверяем, что его еще нет в мапе
        do {
            sessionUID = randomInt();
        }while (UIDMap.containsKey(sessionUID));
        UIDMap.put(sessionUID, user);
        return sessionUID;
    }

    private Integer randomInt(){
        //случайное целое число от 1 до 10001
        return (int) Math.round(Math.random() * maxRandom / 100 + 1);
    }

    public Map<Integer, User> getUIDMap() {
        return UIDMap;
    }

    public void setUIDMap(Map<Integer, User> UIDMap) {
        this.UIDMap = UIDMap;
    }

}
