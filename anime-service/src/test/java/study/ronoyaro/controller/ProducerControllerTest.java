package study.ronoyaro.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.server.ResponseStatusException;
import study.ronoyaro.domain.Producer;
import study.ronoyaro.repository.ProducerData;

import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebMvcTest(controllers = ProducerController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan(basePackages = "study.ronoyaro")
class ProducerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProducerData producerData;

    @Autowired
    private ResourceLoader resourceLoader;
    private List<Producer> producerListMock;

    @BeforeEach
    void init() {

        var dateTime = "2025-12-18T16:39:24.3500076";
        var pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS");
        var localDateTime = LocalDateTime.parse(dateTime, pattern);


        var aniplex = Producer.builder()
                .id(1L)
                .name("Aniplex")
                .createdAt(localDateTime).build();
        var studioGhibli = Producer.builder()
                .id(2L).name("Studio Ghibli")
                .createdAt(localDateTime)
                .build();
        var ufotable = Producer.builder()
                .id(3L)
                .name("Ufotable")
                .createdAt(localDateTime)
                .build();
        producerListMock = new ArrayList<>(List.of(aniplex, studioGhibli, ufotable));
    }

    @Test
    @Order(1)
    @DisplayName("GET /v1/producers returns All Producers when the name Arguments is null")
    void findAll_ReturnsAllProducers_WhenArgumentIsNull() throws Exception {

        BDDMockito.when(producerData.getProducers()).thenReturn(producerListMock);

        var response = readResourceFile("producer/get-producer-null-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/producers"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));

    }

    @Test
    @Order(2)
    @DisplayName("GET /v1/producers?name=Ufotable returns a Producer when the name Arguments exists")
    void findAll_ReturnsAllProducers_WhenNameExists() throws Exception {

        BDDMockito.when(producerData.getProducers()).thenReturn(producerListMock);

        String name = "Ufotable";

        var response = readResourceFile("producer/get-producer-ufotable-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/producers").param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));

    }

    @Test
    @Order(3)
    @DisplayName("GET /v1/producers/{id} returns x when the name doesn't exists")
    void findById_ReturnsAnEmptyList_WhenDoesntExists() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerListMock);

        String name = "x";

        var response = readResourceFile("producer/get-producer-empty-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/producers").param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @Order(4)
    @DisplayName("Find by id returns an Anime when Successful")
    void findById_ReturnsAnAnime_WhenSuccessful() throws Exception {
     BDDMockito.when(producerData.getProducers()).thenReturn(producerListMock);

     Long id = 3L;

        var response = readResourceFile("producer/get-producer-by-id-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/producers/{id}", id))
             .andDo(MockMvcResultHandlers.print())
             .andExpect(MockMvcResultMatchers.status().isOk())
             .andExpect(MockMvcResultMatchers.content().json(response));

    }

    @Test
    @Order(5)
    @DisplayName("Find by id throws a ResponseStatusException when the Anime is not found")
    void findById_ReturnsAnResponseStatusHTTPException_WhenAnimeIsNotFound() throws Exception {
       BDDMockito.when(producerData.getProducers()).thenReturn(producerListMock);

       Long id = 99L;

       mockMvc.perform(MockMvcRequestBuilders.get("/v1/producers/{id}", id))
               .andDo(MockMvcResultHandlers.print())
               .andExpect(MockMvcResultMatchers.status().isNotFound())
               .andExpect(MockMvcResultMatchers.status().reason("Producer not found"));
    }

    private String readResourceFile(String fileName) throws IOException {
        var file = resourceLoader.getResource(ResourceLoader.CLASSPATH_URL_PREFIX.concat(fileName)).getFile();
        return new String(Files.readAllBytes(file.toPath()));
    }

}