package help;

import entity.Stop;

import java.util.ArrayList;
import java.util.List;

public class StopsList {
    public static List<Stop> list = new ArrayList<>();
    static {
        list.add(new Stop("stop__10046034","Менделеева"));
        list.add(new Stop("stop__10046027","Менделеева"));
        list.add(new Stop("stop__10045042","Станция метро Тракторный завод"));
        list.add(new Stop("stop__10045041","Станция метро Тракторный завод"));
    }
}
