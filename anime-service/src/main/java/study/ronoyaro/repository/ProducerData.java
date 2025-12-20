package study.ronoyaro.repository;

import lombok.Getter;
import org.springframework.stereotype.Component;
import study.ronoyaro.domain.Producer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@Getter
public class ProducerData {
    private final List<Producer> producers = new ArrayList<>();

    public ProducerData() {
        var mappa = Producer.builder()
                .id(1L)
                .name("Mappa")
                .createdAt(LocalDateTime.now())
                .build();
        var kyotoAnimation = Producer.builder()
                .id(2L)
                .name("Kyoto Animation")
                .createdAt(LocalDateTime.now())
                .build();
        var madhouse = Producer.builder()
                .id(3L)
                .name("Mad House")
                .createdAt(LocalDateTime.now())
                .build();
        producers.addAll(List.of(mappa, kyotoAnimation, madhouse));
    }

    public List<Producer> getProducers() {
        return producers;
    }

}
