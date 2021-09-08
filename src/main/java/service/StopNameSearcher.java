package service;

import entity.Stop;
import help.StopsList;

import java.util.ArrayList;
import java.util.List;

public class StopNameSearcher {

    public static List<Stop> findStop(String stopName) {
        List<Stop> resList = new ArrayList<>();

        for (Stop stop : StopsList.list) {
            if (stop.getStopName().toLowerCase().startsWith(stopName.toLowerCase())) {
                resList.add(stop);
            }
        }

        if (resList.isEmpty()){
            for (Stop stop : StopsList.list) {
                if (stop.getStopName().toLowerCase().contains(stopName.toLowerCase())) {
                    resList.add(stop);
                    continue;
                }
                for (String strDb : stop.getStopName().split(" ")) {
                    for (String strNew:stopName.split(" ")) {
                        if (strDb.toLowerCase().startsWith(strNew.toLowerCase())) {
                            resList.add(stop);
                        }
                    }

                }
            }
        }

        for (Stop stop : resList) {
            System.out.println(stop);
        }
        return resList;
    }
}
