package businessLogicClasses;

import businessLogicClasses.TypeOfPercent;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PercentTypeFactory {

    private Map<Integer,TypeOfPercent> typeOfPercentMap = new HashMap<>();

    // Подставляются через Spring все классы с интерфейсом TypeOfPercent
    public PercentTypeFactory(List<TypeOfPercent> typeOfPercentList){
        for (TypeOfPercent typeOfPercent: typeOfPercentList) {
            typeOfPercentMap.put(typeOfPercent.getNumTypeOfPercent(),typeOfPercent);
        }
    }

    public Map<Integer, TypeOfPercent> getTypeOfPercentMap() {
        return typeOfPercentMap;
    }

    public void setTypeOfPercentMap(Map<Integer, TypeOfPercent> typeOfPercentMap) {
        this.typeOfPercentMap = typeOfPercentMap;
    }
}
