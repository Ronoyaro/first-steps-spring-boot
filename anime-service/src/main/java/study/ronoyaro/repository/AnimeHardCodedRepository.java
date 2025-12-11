package study.ronoyaro.repository;

import study.ronoyaro.domain.Anime;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AnimeHardCodedRepository {
    private static final List<Anime> ANIMES = new ArrayList<>();

    static {
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
        ANIMES.addAll(List.of(dbz, yuyu, onePiece));
    }

    public List<Anime> listAll() {
        return ANIMES;
    }

    public List<Anime> findByName(String name) {
        return ANIMES.stream()
                .filter(anime -> anime.getName().equalsIgnoreCase(name))
                .toList();
    }

    public Optional<Anime> findById(Long id) {
        return ANIMES.stream()
                .filter(anime -> anime.getId().equals(id))
                .findFirst();
    }

    public Anime save(Anime anime) {
        ANIMES.add(anime);
        return anime;
    }

    public void delete(Anime anime) {
        ANIMES.remove(anime);
    }

    public void update(Anime anime) {
        delete(anime);
        save(anime);
    }
}
