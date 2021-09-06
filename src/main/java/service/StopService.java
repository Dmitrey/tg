package service;

import entity.Stop;
import help.StopsList;

import java.util.List;

public class StopService {

    public List<Stop> getStopList(){
        return StopsList.list;
    }
}
