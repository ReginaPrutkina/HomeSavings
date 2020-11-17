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

    public Security(){
        UIDMap = new HashMap<>();
    }

    public Integer generateAndAddUID(CommonRequest commonRequest){
        Integer sessionUID;
        //генерим UID
        // проверяем, что его еще нет в мапе, или сессия  с таким UID "протухла",
        // и ее можно использовть повторно
        do {
            sessionUID = randomInt();
        }while (isSessionUIDValid(sessionUID));

        commonRequest.setLastUpdate(new Date());
        if (UIDMap.containsKey(sessionUID))
            // Переиспользуем UID неактивной сессии
            UIDMap.replace(sessionUID, commonRequest);
        else
            // Добавляем новый UID
            UIDMap.put(sessionUID, commonRequest);

        return sessionUID;
    }

    public boolean isSessionUIDValid(Integer sessionUID){
        if (sessionUID==null || sessionUID==0)
            return false;
        return UIDMap.containsKey(sessionUID) && this.isSessionActive(sessionUID);
    }

    private boolean isSessionActive(int sessionUID){
        // true, если прошло  не более maxMinutesUnActive мин с lastUpdate
        return ((new Date().getTime() - UIDMap.get(sessionUID).getLastUpdate().getTime()) <= (maxMinutesUnActive*60*1000));
    }

    private Integer randomInt(){
        //случайное целое число от 1 до 10001
        return (int) Math.round(Math.random() * maxRandom  + 1);
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
