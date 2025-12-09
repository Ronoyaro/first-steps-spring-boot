package study.ronoyaro.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


@Getter
@Setter
@Builder
public class Anime {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
    @Getter
    private static List<Anime> animes = new ArrayList<>();

    static {
        var dbz = Anime.builder()
                .id(ThreadLocalRandom.current().nextLong(1, 200))
                .name("Dragon Ball Z")
                .createdAt(LocalDateTime.now())
                .build();

        var yuyu = Anime.builder()
                .id(ThreadLocalRandom.current().nextLong(1, 200))
                .name("Yuyu Hakusho")
                .createdAt(LocalDateTime.now())
                .build();
        var onePiece = Anime.builder()
                .id(ThreadLocalRandom.current().nextLong(1, 200))
                .name("One Piece")
                .createdAt(LocalDateTime.now())
                .build();
        animes.addAll(List.of(dbz, yuyu, onePiece));
    }

    @Override
    public String toString() {
        return "Anime{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
