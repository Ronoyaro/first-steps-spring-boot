package study.ronoyaro.anime.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.ronoyaro.anime.domain.Anime;
import study.ronoyaro.anime.dto.AnimeGetResponse;
import study.ronoyaro.anime.dto.AnimePostRequest;
import study.ronoyaro.anime.dto.AnimePostResponse;
import study.ronoyaro.anime.dto.AnimePutRequest;
import study.ronoyaro.anime.mapper.AnimeMapper;
import study.ronoyaro.anime.service.AnimeService;

import java.util.List;

@RestController
@RequestMapping("v1/animes")
@Slf4j
@RequiredArgsConstructor
public class AnimeController {
    private final AnimeMapper mapper;
    private final AnimeService service;

    @GetMapping
    public ResponseEntity<List<AnimeGetResponse>> findAll(@RequestParam(required = false) String name) {

        log.debug("Request received to list all animes param name '{}'", name);

        var animes = service.findAll(name);

        var animeListGetResponses = mapper.toAnimeListResponses(animes);

        return ResponseEntity.ok(animeListGetResponses);
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<AnimePostResponse>> findAllPaginated(Pageable pageable) {

        log.debug("Request received to list all animes paginated");

        var pageAnime = service.findAllPaginated(pageable).map(mapper::toAnimePostResponse);

        return ResponseEntity.ok(pageAnime);
    }

    @GetMapping("{id}")
    public ResponseEntity<AnimeGetResponse> findById(@PathVariable Long id) {

        log.debug("Request to find Anime by id: '{}'", id);

        Anime anime = service.findByIdOrThrowNotFound(id);

        var animeResponse = mapper.toAnimeResponse(anime);

        return ResponseEntity.ok(animeResponse);
    }

    @PostMapping
    public ResponseEntity<AnimePostResponse> save(@RequestBody @Valid AnimePostRequest animePostRequest) {

        log.debug("Request to save anime '{}'", animePostRequest.getName());

        var anime = mapper.toAnime(animePostRequest);

        Anime animeSaved = service.save(anime);

        var animePostResponse = mapper.toAnimePostResponse(animeSaved);


        return ResponseEntity.status(HttpStatus.CREATED).body(animePostResponse);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteByiId(@PathVariable Long id) {
        log.debug("Request to delete Anime by id '{}'", id);

        service.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody @Valid AnimePutRequest animePutRequest) {
        log.debug("Request to update Anime '{}'", animePutRequest.getName());

        Anime animeToUpdate = mapper.toAnime(animePutRequest);

        service.update(animeToUpdate);

        return ResponseEntity.noContent().build();
    }
}
