package entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class Transport {
    @Getter
    @Setter
    String name;
    @Getter
    @Setter
    String type;
    @Getter
    @Setter
    String estimatedTime;

}
