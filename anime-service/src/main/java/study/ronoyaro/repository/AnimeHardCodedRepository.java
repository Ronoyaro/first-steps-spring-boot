package study.ronoyaro.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import study.ronoyaro.config.Connection;
import study.ronoyaro.config.ConnectionBeanConfiguration;
import study.ronoyaro.domain.Anime;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class AnimeHardCodedRepository {
    private final AnimeData animeData;

    public List<Anime> listAll() {
        return animeData.getAnimes();
    }

    public List<Anime> findByName(String name) {
        return animeData.getAnimes().stream()
                .filter(anime -> anime.getName().equalsIgnoreCase(name))
                .toList();
    }

    public Optional<Anime> findById(Long id) {
        return animeData.getAnimes().stream()
                .filter(anime -> anime.getId().equals(id))
                .findFirst();
    }

    public Anime save(Anime anime) {
        animeData.getAnimes().add(anime);
        return anime;
    }

    public void delete(Anime anime) {
        animeData.getAnimes().remove(anime);
    }

    public void update(Anime anime) {
        delete(anime);
        save(anime);
    }
}
