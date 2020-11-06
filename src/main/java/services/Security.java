package services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class Security {

    private Map<Integer, CommonRequest> UIDMap;

    @Value("10000")
    private int maxRandom;

    @Value("30")
    private int maxMinutesUnActive;

    public boolean containsUID(Integer sessionUID){
        return UIDMap.containsKey(sessionUID);
    }

    public Security(){
        UIDMap = new HashMap<>();
    }

    public Integer generateAndAddUID(CommonRequest commonRequest){
        Integer sessionUID;
        //генерим UID  и проверяем, что его еще нет в мапе
        do {
            sessionUID = randomInt();
        }while (UIDMap.containsKey(sessionUID) && !isSessionOld(sessionUID));
        commonRequest.setLastUpdate(new Date());
        if (UIDMap.containsKey(sessionUID))
            UIDMap.replace(sessionUID, commonRequest);
        else
            UIDMap.put(sessionUID, commonRequest);

        return sessionUID;
    }

    private Integer randomInt(){
        //случайное целое число от 1 до 10001
        return (int) Math.round(Math.random() * maxRandom  + 1);
    }

    public boolean isSessionOld(int sessionUID){
        if ((UIDMap.get(sessionUID) == null) ||
                (UIDMap.get(sessionUID).getLastUpdate() == null))
            return false;
        // true, если прошло более maxMinutesUnActive мин с lastUpdate
        return ((new Date().getTime() - UIDMap.get(sessionUID).getLastUpdate().getTime()) > (maxMinutesUnActive*60*1000));
    }

    public Map<Integer, CommonRequest> getUIDMap() {
        return UIDMap;
    }

    public void setUIDMap(Map<Integer, CommonRequest> UIDMap) {
        this.UIDMap = UIDMap;
    }

    public int getMaxRandom() {
        return maxRandom;
    }

    public int getMaxMinutesUnActive() {
        return maxMinutesUnActive;
    }

    public void setMaxMinutesUnActive(int maxMinutesUnActive) {
        this.maxMinutesUnActive = maxMinutesUnActive;
    }
}
