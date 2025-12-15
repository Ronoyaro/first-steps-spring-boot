package study.ronoyaro.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import study.ronoyaro.domain.Anime;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AnimeHardCodedRepositoryTest {
    @InjectMocks
    private AnimeHardCodedRepository repository;
    @Mock
    private AnimeData animeData;

    private final List<Anime> mockitoAnimes = new ArrayList<>();

    @BeforeEach
    void init() {
        var yugioh = Anime.builder()
                .id(6L)
                .name("Yu Gi Oh - GX")
                .createdAt(LocalDateTime.now())
                .build();

        var kimetsu = Anime.builder()
                .id(8L)
                .name("Kimetsu no Yaiba")
                .createdAt(LocalDateTime.now())
                .build();
        var goblinSlayer = Anime.builder()
                .id(7L)
                .name("Globin Slayer")
                .createdAt(LocalDateTime.now())
                .build();
        mockitoAnimes.addAll(List.of(yugioh, kimetsu, goblinSlayer));
    }

    @Test
    @DisplayName("Find all returns a list with all Animes")
    @Order(1)
    void findAll_ReturnsAllAnimes_WhenSuccessful() {

        BDDMockito.when(animeData.getAnimes()).thenReturn(mockitoAnimes);

        var animesExpcted = repository.listAll();

        Assertions.assertThat(animesExpcted)
                .isNotNull()
                .isNotEmpty()
                .hasSize(mockitoAnimes.size());
    }

    @Test
    @DisplayName("Find by Id returns an anime with given Id")
    @Order(2)
    void findById_ReturnsAnAnime_WhenSucessful() {

        BDDMockito.when(animeData.getAnimes()).thenReturn(mockitoAnimes);

        var animeExpected = mockitoAnimes.getFirst();

        var animeFound = repository.findById(animeExpected.getId());

        Assertions.assertThat(animeFound).isPresent().contains(animeExpected);
    }

    @Test
    @DisplayName("Find by Name returns a empty list when the name parameter is Null")
    @Order(3)
    void findByName_ReturnsAnEmptyList_WhenSucessful() {

        BDDMockito.when(animeData.getAnimes()).thenReturn(mockitoAnimes);

        var listEmptyExpected = repository.findByName(null);

        Assertions
                .assertThat(listEmptyExpected)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("Find by name returns an anime list when the name given exist in the list")
    @Order(4)
    void findByName_ReturnsAnAnimeListIfNameExists_WhenSucessful() {

        BDDMockito.when(animeData.getAnimes()).thenReturn(mockitoAnimes);

        String nameExpectetThatExist = mockitoAnimes.getFirst().getName();

        List<Anime> animeExpected = repository.findByName(nameExpectetThatExist);
        Assertions
                .assertThat(animeExpected)
                .isNotEmpty();
    }

    @Test
    @DisplayName("Save returns an anime save")
    @Order(5)
    void save_ReturnsAnAnime_WhenSucessful() {
        BDDMockito.when(animeData.getAnimes()).thenReturn(mockitoAnimes);

        var animeToSave = Anime.builder()
                .id(10L)
                .name("BokuNoPico")
                .createdAt(LocalDateTime.now())
                .build();
        Anime animeSaved = repository.save(animeToSave);

        var animeOptional = repository.findById(animeSaved.getId());

        Assertions.assertThat(animeToSave).isEqualTo(animeSaved).hasNoNullFieldsOrProperties(); //Compara de um objeto é igual o outro

        Assertions.assertThat(animeOptional).isPresent().contains(animeSaved);
    }

    @Test
    @DisplayName("Delete removes an anime in the list")
    @Order(6)
    void delete_RemovesAnAnime_WhenSucessful() {

        BDDMockito.when(animeData.getAnimes()).thenReturn(mockitoAnimes);

        Anime animeToDelete = mockitoAnimes.getFirst();

        repository.delete(animeToDelete);

        Assertions.assertThat(mockitoAnimes).doesNotContain(animeToDelete);
    }

    @Test
    @DisplayName("Update updates an anime name in the list")
    @Order(7)
    void update_UpdatesAnAnimeName_WhenSucessFul() {

        BDDMockito.when(animeData.getAnimes()).thenReturn(mockitoAnimes);

        Anime animeSource = mockitoAnimes.getFirst();

        var animeToUpdate = Anime.builder()
                .id(animeSource.getId())
                .name("BokuNoPico")
                .createdAt(LocalDateTime.now())
                .build();
        repository.update(animeToUpdate);

        var animeExpected = repository.findByName(animeToUpdate.getName());

        Assertions.assertThat(animeExpected).isNotNull().isNotEmpty();
    }
}