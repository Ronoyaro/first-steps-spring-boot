package study.ronoyaro.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("v1/heroes")
public class HeroesController {
    private static final List<String> HEROES = List.of("Gutts", "Sanji", "Killua");

    @GetMapping
    public List<String> getHeroes() {
        return HEROES;
    }

    @GetMapping("filter")
    public List<String> listHeroesByParams(@RequestParam String name) {
        return HEROES.stream()
                .filter(hero -> hero.equalsIgnoreCase(name))
                .toList();
    }

    @GetMapping("filterList")
    public List<String> listHeroesByParamsList(@RequestParam List<String> names) {
        return HEROES.stream()
                .filter(n -> names.contains(n))
                .toList();
    }
}
