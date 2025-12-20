package study.ronoyaro.commons;

import org.springframework.stereotype.Component;
import study.ronoyaro.domain.Anime;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class MockAnimeListUtils {
    private List<Anime> list;

    public MockAnimeListUtils() {
        var pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS");
        var dateTime = "2025-12-18T16:39:24.3500076";
        var localDateTime = LocalDateTime.parse(dateTime, pattern);

        var steinsGate = Anime.builder()
                .id(1L)
                .name("Steins gate")
                .createdAt(localDateTime)
                .build();

        var fate = Anime.builder()
                .id(2L)
                .name("Fate Zero")
                .createdAt(localDateTime)
                .build();

        var cowboyBebop = Anime.builder()
                .id(3L)
                .name("Cowboy Bebop")
                .createdAt(localDateTime)
                .build();

        list = new ArrayList<>(List.of(steinsGate, fate, cowboyBebop));
    }

    public List<Anime> getList() {
        return list;
    }

}
