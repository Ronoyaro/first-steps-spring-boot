package study.ronoyaro.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.ronoyaro.domain.Anime;
import study.ronoyaro.mapper.AnimeMapper;
import study.ronoyaro.request.AnimePostRequest;

import java.util.List;

@RestController
@RequestMapping("v1/animes")
@Slf4j
public class AnimeController {
    public static final AnimeMapper MAPPER = AnimeMapper.INSTANCE;

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
    public ResponseEntity<Anime> save(@RequestBody AnimePostRequest animePostRequest) {

        var anime = MAPPER.toAnime(animePostRequest);
        var animeResponse = MAPPER.toAnimeResponse(anime);

        log.info("anime saved'{}'", animeResponse);

        Anime.getAnimes()
                .add(anime);

        return ResponseEntity.status(HttpStatus.CREATED).body(anime);
    }
}
