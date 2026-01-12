package study.ronoyaro.controller;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import study.ronoyaro.anime.AnimeController;
import study.ronoyaro.commons.FileUtils;
import study.ronoyaro.commons.MockAnimeListUtils;
import study.ronoyaro.domain.Anime;
import study.ronoyaro.anime.AnimeRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@WebMvcTest(controllers = AnimeController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan(basePackages = {"study.ronoyaro.anime", "study.ronoyaro.commons"})
@Log4j2
class AnimeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FileUtils fileUtils;

    @MockBean
    private AnimeRepository repository;

    @Autowired
    private MockAnimeListUtils mockAnimeListUtils;

    private List<Anime> animeList;

    @BeforeEach
    void init() {
        animeList = mockAnimeListUtils.getList();
    }

    @Test
    @Order(1)
    @DisplayName("GET /v1/animes list all animes when the argument name is null")
    void listAll_ReturnsAllAnimes_WhenNameArgumentIsNull() throws Exception {
        BDDMockito.when(repository.findAll()).thenReturn(animeList);

        var response = fileUtils.readResourceFile("anime/get-anime-response-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/animes"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @Order(2)
    @DisplayName("GET /v1/animes?name='Fate Zero' returns a animes list that matches with the given argument name")
    void listAll_ReturnsAnAnimeList_WhenAnimeNameExists() throws Exception {
        String name = "Fate Zero";

        var listAnimeExpected = animeList.stream()
                .filter(anime -> anime.getName().equalsIgnoreCase(name))
                .toList();

        BDDMockito.when(repository.findByNameIgnoreCase(name)).thenReturn(listAnimeExpected);

        var response = fileUtils.readResourceFile("anime/get-anime-fatezero-response-200.json");


        mockMvc.perform(MockMvcRequestBuilders.get("/v1/animes").param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @Order(3)
    @DisplayName("GET /v1/animes returns an empty list when the arguments name doesn't exists")
    void listAll_ReturnsAnEmptyList_WhenNameDoesntExists() throws Exception {

        String name = "x";

        var response = fileUtils.readResourceFile("anime/get-anime-empty-response-200.json");

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
    @DisplayName("GET /v1/animes/{id} returns a ResponseStatusException 404 when the anime is not found")
    void findById_ReturnsAResponseStatus404_WhenTheAnimeDoesntExists() throws Exception {

        Long id = 99L;

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/animes/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("Anime not found"));
    }

    @Test
    @Order(6)
    @DisplayName("POST /v1/animes creates an Anime when successful")
    void save_CreatesAnAnime_whenSuccessful() throws Exception {

        String dateTime = "2025-12-18T16:39:24.3500076";
        var pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS");
        var localDateTime = LocalDateTime.parse(dateTime, pattern);
        var soloLeveling = Anime.builder()
                .id(10L)
                .name("Solo Leveling")
                .createdAt(localDateTime)
                .build();

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
    @DisplayName("PUT v1/animes throws an ResponseStatusException 404 when the anime doesnt exists")
    void update_ThrowsResponseStatusException_WhenAnimeNotFound() throws Exception {

        var request = fileUtils.readResourceFile("anime/put-anime-request-404.json");


        mockMvc.perform(MockMvcRequestBuilders.put("/v1/animes")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("Anime not found"));
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
    @DisplayName("DELETE v1/animes/{id} throws ResponseStatusHTTPException when Anime is Not Found")
    void delete_throwsResponseStatusException404_whenAnimeIsNotFound() throws Exception {

        Long id = 99L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/animes/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("Anime not found"));
    }


}