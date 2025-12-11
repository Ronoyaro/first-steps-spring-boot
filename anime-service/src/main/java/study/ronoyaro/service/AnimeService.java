package study.ronoyaro.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import study.ronoyaro.domain.Anime;
import study.ronoyaro.repository.AnimeHardCodedRepository;

import java.util.List;

public class AnimeService {
    private AnimeHardCodedRepository repository;

    public AnimeService() {
        this.repository = new AnimeHardCodedRepository();
    }

    public List<Anime> findAll(String name) {
        return name == null ? repository.listAll() : repository.findByName(name);
    }

    public Anime findByIdOrThrowNotFound(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Anime not found"));
    }

    public Anime save(Anime anime) {
        return repository.save(anime);
    }

    public void deleteById(Long id) {
        Anime anime = findByIdOrThrowNotFound(id);
        repository.delete(anime);
    }

    public void update(Anime animeToUpdate) {
        Anime anime = findByIdOrThrowNotFound(animeToUpdate.getId());
        animeToUpdate.setCreatedAt(anime.getCreatedAt());
        repository.update(animeToUpdate);
    }
}
