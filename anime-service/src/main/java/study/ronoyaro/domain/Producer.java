package study.ronoyaro.domain;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class Producer {
    private Long id;
    @JsonProperty("name") //Serve para serialização do Jackson quando a requisição pode vir com outro nome no atributo
    private String name;
    @Getter
    private static List<Producer> producers = new ArrayList<>();

    static {
        var mappa = new Producer(1L, "Mappa");
        var kyotoAnimation = new Producer(2L, "Kyoto Animation");
        var madhouse = new Producer(3L, "Mad House");
        producers.addAll(List.of(mappa, kyotoAnimation, madhouse));
    }

    @Override
    public String toString() {
        return "Producer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
