package study.ronoyaro.service;

import lombok.NonNull;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import study.ronoyaro.domain.Anime;
import study.ronoyaro.repository.AnimeHardCodedRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AnimeServiceTest {
    @InjectMocks
    private AnimeService service;
    @Mock
    private AnimeHardCodedRepository repository;
    private List<Anime> mockitoAnime;

    @BeforeEach
    void init() {
        Anime deathNote = Anime.builder()
                .id(1L)
                .name("Death Note")
                .createdAt(LocalDateTime.now())
                .build();

        Anime vinlandSaga = Anime.builder()
                .id(2L)
                .name("Vinland Saga")
                .createdAt(LocalDateTime.now())
                .build();

        Anime fullMetal = Anime.builder()
                .id(3L)
                .name("Full Metal Alchemist: Brotherhood")
                .build();
        mockitoAnime = new ArrayList<>(List.of(deathNote, fullMetal, vinlandSaga));
    }

    @Test
    @Order(1)
    @DisplayName("listAll returns an Anime List when the name argument is null")
    void listAll_ReturnsAnAnimeList_WhenNameArgumentIsNull() {

        BDDMockito.when(repository.listAll()).thenReturn(mockitoAnime);

        var listAnimeExpected = service.findAll(null);

        Assertions.assertThat(listAnimeExpected)
                .hasSameElementsAs(mockitoAnime);

    }

    @Test
    @Order(2)
    @DisplayName("listAll returns a list with animes founds when the name passed by argument exists")
    void listAll_ReturnsAnAnimeList_WhenTheNameArgumentExists() {
        var anime = mockitoAnime.getFirst();

        var singletonList = singletonList(anime);

        BDDMockito.when(repository.findByName(anime.getName())).thenReturn(singletonList);

        var animeListExpected = service.findAll(anime.getName());

        Assertions.assertThat(animeListExpected)
                .containsAll(singletonList);
    }

    @Test
    @Order(3)
    @DisplayName("listAll returns an empty list when the name is not found")
    void listAll_ReturnsAnEmptyList_WhenTheNameIsNotFound() {

        BDDMockito.when(repository.findByName("xaxa")).thenReturn(Collections.emptyList());

        var animeListEmptyExpected = service.findAll("xaxa");

        Assertions.assertThat(animeListEmptyExpected)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @Order(4)
    @DisplayName("Find by id returns an Anime when Successful")
    void findById_ReturnsAnAnime_WhenSuccessful() {
        var anime = mockitoAnime.getFirst();

        BDDMockito.when(repository.findById(anime.getId())).thenReturn(Optional.of(anime));

        Assertions.assertThatNoException().isThrownBy(() -> service.findByIdOrThrowNotFound(anime.getId()));

    }

    @Test
    @Order(5)
    @DisplayName("Find by id throws a ResponseStatusException when the Anime is not found")
    void findById_ReturnsAnResponseStatusHTTPException_WhenAnimeIsNotFound() {
        var anime = mockitoAnime.getFirst();

        BDDMockito.when(repository.findById(anime.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.findByIdOrThrowNotFound(anime.getId()))
                .isInstanceOf(ResponseStatusException.class);

    }

    @Test
    @Order(6)
    @DisplayName("save creates an anime when successful")
    void save_CreatesAnAnime_WhenSucessful() {
        var animeToSave = Anime.builder()
                .id(6L)
                .name("Solo Leveling")
                .createdAt(LocalDateTime.now())
                .build();

        BDDMockito.when(repository.save(animeToSave)).thenReturn(animeToSave);

        Anime animeSaved = service.save(animeToSave);

        Assertions.assertThat(animeSaved).isEqualTo(animeToSave);

    }

    @Test
    @Order(7)
    @DisplayName("Delete removes an anime when the given id exists")
    void delete_RemovesAnAnime_WhenSucessful() {

        var animeToDelete = mockitoAnime.getFirst();

        BDDMockito.when(repository.findById(animeToDelete.getId())).thenReturn(Optional.of(animeToDelete));

        BDDMockito.doNothing().when(repository).delete(animeToDelete);

        Assertions.assertThatNoException().isThrownBy(() -> service.delete(animeToDelete.getId()));

    }

    @Test
    @Order(8)
    @DisplayName("Deletes throws a ResponseStatusException when the anime doesnt exists")
    void delete_ThrowsAException_WhenTheAnimeIsNotFound() {

        Anime animeToDelete = mockitoAnime.getFirst();

        BDDMockito.when(repository.findById(animeToDelete.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.delete(animeToDelete.getId()))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @Order(9)
    @DisplayName("Update updates an anime when successful")
    void update_UpdatesAnAnime_whenSuccessful() {
        Anime animeToUpdate = mockitoAnime.getFirst();

        animeToUpdate.setName("Inazuma Eleven");

        BDDMockito.when(repository.findById(animeToUpdate.getId())).thenReturn(Optional.of(animeToUpdate));

        BDDMockito.doNothing().when(repository).update(animeToUpdate);

        Assertions.assertThatNoException()
                .isThrownBy(() -> service.update(animeToUpdate));
    }

    @Test
    @Order(10)
    @DisplayName("Updates throws a ResponseStatusException when the anime doesnt exists")
    void update_ThrowsAnException_WhenTheAnimeIsNotFound(){
        Anime animeToUpdate = mockitoAnime.getFirst();

        animeToUpdate.setName("Saint Seiya");

        BDDMockito.when(repository.findById(animeToUpdate.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.update(animeToUpdate))
                .isInstanceOf(ResponseStatusException.class);


    }




    private static @NonNull List<Anime> singletonList(Anime listAnimesExpected) {
        return Collections.singletonList(listAnimesExpected);
    }


}
