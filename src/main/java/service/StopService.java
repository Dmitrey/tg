package service;

import entity.Stop;

import java.util.ArrayList;
import java.util.List;

public class StopService {

    public List<Stop> getStopList(){

        List<Stop> stopList = new ArrayList<>();
        Stop stop0 = new Stop("менделеева","stop__10046027","2015839121","ymapsbm1%3A%2F%2Ftransit%2Fstop%3Fid%3Dstop__10046027");
        Stop stop1 = new Stop("тракторный завод","stop__10045041","2841804401","ymapsbm1%3A%2F%2Ftransit%2Fstop%3Fid%3Dstop__10045041");
        stopList.add(stop0);
        stopList.add(stop1);
        return stopList;

    }
}
