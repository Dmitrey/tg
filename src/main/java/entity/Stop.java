package entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class Stop {
    @Getter
    @Setter
    private String id;
    @Getter
    @Setter
    private String stopName;

    @Override
    public String toString() {
        return "Stop{" +
                "id='" + id + '\'' +
                ", stopName='" + stopName + '\'' +
                '}';
    }
}
