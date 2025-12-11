package study.ronoyaro.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.ronoyaro.domain.Anime;
import study.ronoyaro.mapper.AnimeMapper;
import study.ronoyaro.request.AnimePostRequest;
import study.ronoyaro.request.AnimePutRequest;
import study.ronoyaro.response.AnimeGetResponse;
import study.ronoyaro.response.AnimePostResponse;
import study.ronoyaro.service.AnimeService;

import java.util.List;

@RestController
@RequestMapping("v1/animes")
@Slf4j
public class AnimeController {
    public static final AnimeMapper MAPPER = AnimeMapper.INSTANCE;
    private final AnimeService service;

    public AnimeController() {
        this.service = new AnimeService();
    }

    @GetMapping
    public ResponseEntity<List<AnimeGetResponse>> list(@RequestParam(required = false) String name) {

        log.debug("Request received to list all Animes param name '{}'", name);

        var animes = service.findAll(name);

        var animeListGetResponses = MAPPER.toAnimeListResponses(animes);

        return ResponseEntity.ok(animeListGetResponses);
    }

    @GetMapping("{id}")
    public ResponseEntity<AnimeGetResponse> filterById(@PathVariable Long id) {

        log.debug("Request to find Anime by id: '{}'", id);

        Anime anime = service.findByIdOrThrowNotFound(id);

        var animeResponse = MAPPER.toAnimeResponse(anime);

        return ResponseEntity.ok(animeResponse);
    }

    @PostMapping
    public ResponseEntity<AnimePostResponse> save(@RequestBody AnimePostRequest animePostRequest) {

        log.debug("Request to save anime '{}'", animePostRequest.getName());

        var anime = MAPPER.toAnime(animePostRequest);

        Anime animeSaved = service.save(anime);

        var animePostResponse = MAPPER.toAnimePostResponse(animeSaved);


        return ResponseEntity.status(HttpStatus.CREATED).body(animePostResponse);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteByiId(@PathVariable Long id) {
        log.debug("Request to delete Anime by id '{}'", id);

        service.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody AnimePutRequest animePutRequest) {
        log.debug("Request to update Anime '{}'", animePutRequest.getName());

        Anime animeToUpdate = MAPPER.toAnime(animePutRequest);

        service.update(animeToUpdate);

        return ResponseEntity.noContent().build();
    }
}
