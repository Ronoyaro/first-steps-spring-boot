package study.ronoyaro.commons;

import org.springframework.stereotype.Component;
import study.ronoyaro.anime.domain.Anime;

import java.util.ArrayList;
import java.util.List;

@Component
public class MockAnimeListUtils {
    private final List<Anime> list;

    public MockAnimeListUtils() {

        var steinsGate = Anime.builder()
                .id(1L)
                .name("Steins gate")
                .build();

        var fate = Anime.builder()
                .id(2L)
                .name("Fate Zero")
                .build();

        var cowboyBebop = Anime.builder()
                .id(3L)
                .name("Cowboy Bebop")
                .build();

        list = new ArrayList<>(List.of(steinsGate, fate, cowboyBebop));
    }

    public List<Anime> getList() {
        return list;
    }

    public Anime newAnime() {
        return Anime.builder()
                .id(1L)
                .name("Solo Leveling")
                .build();
    }

}
