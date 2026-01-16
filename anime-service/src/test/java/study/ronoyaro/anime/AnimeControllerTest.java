package study.ronoyaro.anime;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import study.ronoyaro.anime.controller.AnimeController;
import study.ronoyaro.anime.domain.Anime;
import study.ronoyaro.anime.repository.AnimeRepository;
import study.ronoyaro.commons.FileUtils;
import study.ronoyaro.commons.MockAnimeListUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@WebMvcTest(controllers = AnimeController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan(basePackages = {"study.ronoyaro.anime", "study.ronoyaro.commons"})
class AnimeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FileUtils fileUtils;

    @MockBean
    private AnimeRepository repository;

    @Autowired
    private MockAnimeListUtils animeListUtils;

    private List<Anime> animeList;

    @BeforeEach
    void init() {
        animeList = animeListUtils.getList();
    }

    @Test
    @Order(1)
    @DisplayName("GET /v1/animes list all animes when the argument name is null")
    void findAll_ReturnsAllAnimes_WhenNameArgumentIsNull() throws Exception {
        BDDMockito.when(repository.findAll()).thenReturn(animeList);

        var response = fileUtils.readResourceFile("anime/get-anime-null-argument-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/animes"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @Order(1)
    @DisplayName("GET /v1/animes/paginated returns a paginated list with all animes")
    void findAllPaginated_ReturnsAnimesPaginated_WhenSuccessful() throws Exception {
        BDDMockito.when(repository.findAll()).thenReturn(animeList);

        var response = fileUtils.readResourceFile("anime/get-anime-paginated-200.json");

        var pageRequest = PageRequest.of(0, animeList.size());
        var animePage = new PageImpl<Anime>(animeList, pageRequest, 1);

        BDDMockito.when(repository.findAll(BDDMockito.any(Pageable.class))).thenReturn(animePage);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/animes/paginated"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @Order(2)
    @DisplayName("GET /v1/animes?name=Fate+Zero returns a animes list that matches with the given argument name")
    void findAll_ReturnsAnAnimeList_WhenAnimeNameExists() throws Exception {
        var response = fileUtils.readResourceFile("anime/get-anime-fatezero-response-200.json");

        String name = "Fate Zero";

        var listAnimeExpected = animeList.stream()
                .filter(anime -> anime.getName().equalsIgnoreCase(name))
                .toList();

        BDDMockito.when(repository.findByNameIgnoreCase(name)).thenReturn(listAnimeExpected);


        mockMvc.perform(MockMvcRequestBuilders.get("/v1/animes").param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @Order(3)
    @DisplayName("GET /v1/animes?name=x returns an empty list when the arguments name doesn't exists")
    void findAll_ReturnsAnEmptyList_WhenNameDoesntExists() throws Exception {
        var response = fileUtils.readResourceFile("anime/get-anime-empty-response-200.json");

        String name = "x";

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/animes").param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @Order(4)
    @DisplayName("GET /v1/animes/{id} returns an anime with id given")
    void findById_ReturnsAnAnimeById_WhenSuccessful() throws Exception {

        var response = fileUtils.readResourceFile("anime/get-anime-by-id-response-200.json");

        Long id = 2L;

        var animeFound = animeList.stream()
                .filter(anime -> anime.getId().equals(id))
                .findFirst();

        BDDMockito.when(repository.findById(id)).thenReturn(animeFound);


        mockMvc.perform(MockMvcRequestBuilders.get("/v1/animes/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));

    }

    @Test
    @Order(5)
    @DisplayName("GET /v1/animes/{id} throws a NotFoundException when the anime is not found")
    void findById_ThrowsNotFoundException_WhenTheAnimeDoesntExists() throws Exception {

        Long id = 99L;

        var response = fileUtils.readResourceFile("anime/get-anime-by-id-not-found-404-reponse.json");


        mockMvc.perform(MockMvcRequestBuilders.get("/v1/animes/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @Order(6)
    @DisplayName("POST /v1/animes creates an Anime when successful")
    void save_CreatesAnAnime_whenSuccessful() throws Exception {

        var soloLeveling = animeListUtils.newAnime();


        var request = fileUtils.readResourceFile("anime/post-anime-request-200.json");
        var response = fileUtils.readResourceFile("anime/post-anime-response-201.json");

        BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(soloLeveling);


        mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/animes")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(request)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(response));

    }

    @Test
    @Order(7)
    @DisplayName("PUT v1/animes updates an anime")
    void update_UpdatesAnAnime_WhenSuccessful() throws Exception {

        var request = fileUtils.readResourceFile("anime/put-anime-request-204.json");

        Long id = 1L;
        var animeFound = animeList.stream()
                .filter(anime -> anime.getId().equals(id))
                .findFirst();

        BDDMockito.when(repository.findById(id)).thenReturn(animeFound);

        mockMvc.perform(MockMvcRequestBuilders.put("/v1/animes")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @Order(8)
    @DisplayName("PUT v1/animes throws a NotFoundException when the anime doesnt exists")
    void update_ThrowsNotFoundException_WhenAnimeNotFound() throws Exception {

        var request = fileUtils.readResourceFile("anime/put-anime-request-404.json");
        var response = fileUtils.readResourceFile("anime/get-anime-by-id-not-found-404-reponse.json");


        mockMvc.perform(MockMvcRequestBuilders.put("/v1/animes")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @Order(9)
    @DisplayName("DELETE v1/animes/{id} removes an Anime")
    void delete_RemovesAnAnime_When_Successful() throws Exception {
        Long id = 1L;
        var animeFound = animeList.stream()
                .filter(anime -> anime.getId().equals(id))
                .findFirst();
        BDDMockito.when(repository.findById(id)).thenReturn(animeFound);

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/animes/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());


    }


    @Test
    @Order(10)
    @DisplayName("DELETE v1/animes/{id} throws NotFoundException when Anime is Not Found")
    void delete_throwsNotFoundException_whenAnimeIsNotFound() throws Exception {

        Long id = 99L;
        var response = fileUtils.readResourceFile("anime/get-anime-by-id-not-found-404-reponse.json");


        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/animes/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @ParameterizedTest
    @MethodSource("postBadRequestSource")
    @Order(11)
    @DisplayName("POST v1/animes throws a bad request when field name is empty")
    void post_throwsBadRequest_WhenNameIsEmpty(String path, List<String> errors) throws Exception {

        String request = fileUtils.readResourceFile(path);

        var result = mockMvc.perform(MockMvcRequestBuilders.post("/v1/animes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        var exception = result.getResolvedException();
        Assertions.assertThat(exception.getMessage()).contains(errors);
    }

    @ParameterizedTest
    @Order(12)
    @MethodSource(value = "putBadRequestSource")
    @DisplayName("PUT v1/animes throws a bad request when the name field is empty")
    void put_throwsBadRequest_WhenNameIsEmpty(String path, List<String> errors) throws Exception {

        var request = fileUtils.readResourceFile(path);

        var result = mockMvc.perform(MockMvcRequestBuilders.put("/v1/animes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
        var exception = result.getResolvedException();

        Assertions.assertThat(exception.getMessage()).contains(errors);
    }

    private static Stream<Arguments> putBadRequestSource() {
        var nameField = "The field 'name' is required";
        var errors = Collections.singletonList(nameField);

        return Stream.of(
                Arguments.of("anime/put-anime-empty-400-request.json", errors)
        );
    }

    private static Stream<Arguments> postBadRequestSource() {
        var nameField = "The field 'name' is required";
        var errors = Collections.singletonList(nameField);
        return Stream.of(
                Arguments.of("anime/post-anime-empty-400-request.json", errors)
        );


    }


}