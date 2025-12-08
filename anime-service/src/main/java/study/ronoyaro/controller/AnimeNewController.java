package study.ronoyaro.controller;

import org.springframework.web.bind.annotation.*;
import study.ronoyaro.domain.Anime;

import java.util.List;

@RestController
@RequestMapping("v1/animes")
public class AnimeNewController {
    @GetMapping
    public List<Anime> listAll(@RequestParam(required = false) String name) {
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


}
