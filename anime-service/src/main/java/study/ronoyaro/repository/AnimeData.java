package study.ronoyaro.repository;

import org.springframework.stereotype.Component;
import study.ronoyaro.domain.Anime;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component

public class AnimeData {
    private final List<Anime> animes = new ArrayList<>();

    public AnimeData() {
        var dbz = Anime.builder()
                .id(1L)
                .name("Dragon Ball Z")
                .createdAt(LocalDateTime.now())
                .build();

        var yuyu = Anime.builder()
                .id(2L)
                .name("Yuyu Hakusho")
                .createdAt(LocalDateTime.now())
                .build();
        var onePiece = Anime.builder()
                .id(3L)
                .name("One Piece")
                .createdAt(LocalDateTime.now())
                .build();
        animes.addAll(List.of(dbz, yuyu, onePiece));
    }

    public List<Anime> getAnimes() {
        return animes;
    }


}
