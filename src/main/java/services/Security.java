package services;

import dataClasses.User;

import java.util.Map;

public class Security {
    private Map<Integer, User> UIDMap;

    public boolean containUID(Integer sessionUID){
        return UIDMap.containsKey(sessionUID);
    }

    public void addUID(Integer sessionUID, User user){
        UIDMap.put(sessionUID, user);
    }

    public Map<Integer, User> getUIDMap() {
        return UIDMap;
    }

    public void setUIDMap(Map<Integer, User> UIDMap) {
        this.UIDMap = UIDMap;
    }
}
