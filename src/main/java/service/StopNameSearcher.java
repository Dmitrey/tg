package service;

import entity.Stop;
import help.StopsList;

import java.util.ArrayList;
import java.util.List;

public class StopNameSearcher {

    public static List<Stop> findStop(String stopName) {
        List<Stop> resList = new ArrayList<>();
        for (Stop stop : StopsList.list) {
            for (String str : stop.getStopName().split(" ")) {
                if (str.toLowerCase().startsWith(stopName.toLowerCase())) {
                    resList.add(stop);
                }
            }
        }
        for (Stop stop : resList) {
            System.out.println(stop);
        }
        return resList;
    }
}
