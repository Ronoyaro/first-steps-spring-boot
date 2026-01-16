package study.ronoyaro.anime.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.ronoyaro.anime.domain.Anime;

import java.util.List;

public interface AnimeRepository extends JpaRepository<Anime, Long> {
    List<Anime> findByNameIgnoreCase(String name);
}
