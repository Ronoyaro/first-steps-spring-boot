package study.ronoyaro.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import study.ronoyaro.domain.Anime;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("v1/animes")
@Slf4j
public class AnimeNewController {
    @GetMapping
    public List<Anime> list(@RequestParam(required = false) String name) {
        if (name == null) return Anime.getAnimes();

        return Anime.getAnimes().stream()
                .filter(a -> a.getName().equals(name))
                .toList();
    }

    @GetMapping("{id}")
    public Anime filterById(@PathVariable Long id) {
        return Anime.getAnimes()
                .stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @PostMapping
    public Anime save(@RequestBody Anime anime) {
        anime.setId(ThreadLocalRandom.current().nextLong(1, 300));
        Anime.getAnimes().add(anime);
        log.info("anime '{}' saved", anime);
        return anime;
    }
}
