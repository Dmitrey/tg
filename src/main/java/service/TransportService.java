package service;

import entity.Stop;
import entity.Transport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class TransportService {

//    public Transport getTransport(String key){
//        List<Transport> list = MyRequest.getTransportList();
//        Map<String,Transport> map = new HashMap<>();
//        for (Transport t: list) {
//            map.put(t.getType()+t.getName(),t);
//        }
//        return map.get(key);
//    }

    public static List<Transport> getTransportByStops(List<Stop> stopList) {
        List<Transport> transportList = new ArrayList<>();
        for (Stop stop: stopList) {
            transportList.addAll(MyRequest.getTransports(stop));
        }
        return transportList;
    }

    public static List<Transport> getTransportByStop(Stop stop) {
        return MyRequest.getTransports(stop);
    }
}
