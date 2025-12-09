package study.ronoyaro.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import study.ronoyaro.domain.Anime;
import study.ronoyaro.mapper.AnimeMapper;
import study.ronoyaro.request.AnimePostRequest;
import study.ronoyaro.request.AnimePutRequest;
import study.ronoyaro.response.AnimeGetResponse;
import study.ronoyaro.response.AnimePostResponse;

import java.util.List;

@RestController
@RequestMapping("v1/animes")
@Slf4j
public class AnimeController {
    public static final AnimeMapper MAPPER = AnimeMapper.INSTANCE;
    public static final String ANIME_NOT_FOUND = "Anime not found";

    @GetMapping
    public ResponseEntity<List<AnimeGetResponse>> list(@RequestParam(required = false) String name) {

        log.debug("Request received to list all Animes param name '{}'", name);

        if (name == null) {
            var animesGetResponse = Anime.getAnimes()
                    .stream()
                    .map(MAPPER::toAnimeResponse)
                    .toList();
            return ResponseEntity.ok(animesGetResponse);
        }
        var animesGetResponse = Anime.getAnimes().stream()
                .filter(a -> a.getName().equals(name))
                .map(MAPPER::toAnimeResponse)
                .toList();
        return ResponseEntity.ok(animesGetResponse);
    }

    @GetMapping("{id}")
    public ResponseEntity<AnimeGetResponse> filterById(@PathVariable Long id) {

        log.debug("Request to find Anime by id: '{}'", id);

        var animeGetResponse = Anime.getAnimes()
                .stream()
                .map(MAPPER::toAnimeResponse)
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ANIME_NOT_FOUND));
        return ResponseEntity.ok(animeGetResponse);
    }

    @PostMapping
    public ResponseEntity<AnimePostResponse> save(@RequestBody AnimePostRequest animePostRequest) {
        var anime = MAPPER.toAnime(animePostRequest);
        var animePostResponse = MAPPER.toAnimePostResponse(anime);

        log.info("Request to save anime '{}'", animePostResponse);


        Anime.getAnimes()
                .add(anime);

        return ResponseEntity.status(HttpStatus.CREATED).body(animePostResponse);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteByiId(@PathVariable Long id) {
        log.debug("Request to delete Anime by id '{}'", id);
        var animeToDelete = Anime.getAnimes().stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ANIME_NOT_FOUND));

        Anime.getAnimes().remove(animeToDelete);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody AnimePutRequest animePutRequest) {
        log.debug("Request to update Anime '{}'", animePutRequest.getName());
        var animeToRemove = Anime.getAnimes().stream()
                .filter(a -> a.getId().equals(animePutRequest.getId()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ANIME_NOT_FOUND));
        var animeToUpdate = MAPPER.toAnime(animePutRequest, animeToRemove.getCreatedAt());
        Anime.getAnimes().remove(animeToRemove);
        Anime.getAnimes().add(animeToUpdate);
        return ResponseEntity.noContent().build();
    }
}
