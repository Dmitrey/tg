package entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class Transport {
//    @Getter
//    @Setter
//    String idTransport; //idThread from yand
    @Getter
    @Setter
    String fullName;
    @Getter
    @Setter
    String name;
    @Getter
    @Setter
    String direction;
    @Getter
    @Setter
    String type;
    @Getter
    @Setter
    String estimatedTime;
    @Getter
    @Setter
    String stopId; //на какой остановке выбран транспорт

}
