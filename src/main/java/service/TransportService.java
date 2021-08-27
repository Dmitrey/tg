package service;

import entity.Transport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class TransportService {

    public Transport getTransport(String key){
        List<Transport> list = MyRequest.getTransportList();
        Map<String,Transport> map = new HashMap<>();
        for (Transport t: list) {
            map.put(t.getType()+t.getName(),t);
        }
        return map.get(key);
    }
}
