package study.ronoyaro.commons;

import org.springframework.stereotype.Component;
import study.ronoyaro.domain.Producer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class MockProducerListUtils {
    private List<Producer> list;

    public MockProducerListUtils() {
        var dateTime = "2025-12-18T16:39:24.3500076";
        var pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS");
        var localDateTime = LocalDateTime.parse(dateTime, pattern);

        var aniplex = Producer.builder()
                .id(1L)
                .name("Aniplex")
                .createdAt(localDateTime)
                .build();

        var studioGhibli = Producer.builder()
                .id(2L).name("Studio Ghibli")
                .createdAt(localDateTime)
                .build();

        var ufotable = Producer
                .builder()
                .id(3L)
                .name("Ufotable")
                .createdAt(localDateTime)
                .build();

        list = new ArrayList<>(List.of(aniplex, studioGhibli, ufotable));
    }

    public List<Producer> getList() {
        return list;
    }
}
