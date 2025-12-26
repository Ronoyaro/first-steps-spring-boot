package study.ronoyaro.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import study.ronoyaro.commons.FileUtils;
import study.ronoyaro.commons.MockProducerListUtils;
import study.ronoyaro.domain.Producer;
import study.ronoyaro.repository.ProducerData;
import study.ronoyaro.repository.ProducerHardCodedRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@WebMvcTest(controllers = ProducerController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan(basePackages = "study.ronoyaro")
class ProducerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProducerData producerData;

    @Autowired
    private MockProducerListUtils producerListUtils;

    private List<Producer> producerList;

    @Autowired
    private FileUtils fileUtils;

    @SpyBean
    private ProducerHardCodedRepository repository;


    @BeforeEach
    void init() {
        producerList = producerListUtils.getList();
    }


    @Test
    @Order(1)
    @DisplayName("GET /v1/producers returns All Producers when the name Arguments is null")
    void findAll_ReturnsAllProducers_WhenArgumentIsNull() throws Exception {

        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        var response = fileUtils.readResourceFile("producer/get-producer-null-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/producers"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));

    }

    @Test
    @Order(2)
    @DisplayName("GET /v1/producers?name=Ufotable returns a Producer when the name Arguments exists")
    void findAll_ReturnsAllProducers_WhenNameExists() throws Exception {

        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        String name = "Ufotable";

        var response = fileUtils.readResourceFile("producer/get-producer-ufotable-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/producers").param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));

    }

    @Test
    @Order(3)
    @DisplayName("GET /v1/producers/{id} returns x when the name doesn't exists")
    void findById_ReturnsAnEmptyList_WhenDoesntExists() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        String name = "x";

        var response = fileUtils.readResourceFile("producer/get-producer-empty-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/producers").param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @Order(4)
    @DisplayName("GET /v1/producers/3 id returns an Anime when Successful")
    void findById_ReturnsAnAnime_WhenSuccessful() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        Long id = 3L;

        var response = fileUtils.readResourceFile("producer/get-producer-by-id-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/producers/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));

    }

    @Test
    @Order(5)
    @DisplayName("GET /v1/producers/99 throws a ResponseStatusException when the Anime is not found")
    void findById_ReturnsAnResponseStatusHTTPException_WhenAnimeIsNotFound() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        Long id = 99L;

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/producers/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("Producer not found"));
    }

    @Test
    @Order(6)
    @DisplayName("POST /v1/producers creates a new Producer")
    void save_CreatesAProducer_WhenSuccessful() throws Exception {

        var dateTime = "2025-12-18T16:39:24.3500076";
        var pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS");
        var localDateTime = LocalDateTime.parse(dateTime, pattern);

        Producer producerToSave = Producer
                .builder()
                .id(99L)
                .name("Ufotable")
                .createdAt(localDateTime)
                .build();

        var request = fileUtils.readResourceFile("producer/post-producer-request-200.json");
        var response = fileUtils.readResourceFile("producer/post-producer-response-201.json");

        BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(producerToSave);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/producers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("x-api-key", "1234")
                        .content(request)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @Order(7)
    @DisplayName("PUT /v1/producers updates a producer")
    void update_UpdatesAProducer_WhenSuccessful() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        var request = fileUtils.readResourceFile("producer/put-producer-request-200.json");

        mockMvc.perform(MockMvcRequestBuilders.put("/v1/producers")
                        .header("x-api-key", "1234")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @Order(8)
    @DisplayName("PUT /v1/producers throws a ResponseStatusException 404 when Producer was not found")
    void update_ThrowsResponseStatusException404_WhenProducerNotFound() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        var request = fileUtils.readResourceFile("producer/put-producer-request-404.json");

        mockMvc.perform(MockMvcRequestBuilders.put("/v1/producers")
                        .header("x-api-key", "1234")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }


    @Test
    @Order(9)
    @DisplayName("DELETE /v1/producers/{id} removes a producer when producer exists in the list")
    void delete_RemoveProducer_WhenSuccessful() throws Exception {

        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        Long id = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/producers/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());

    }

    @Test
    @Order(10)
    @DisplayName("DELETE /v1/producers/{id} throws a ResponseStatusException when producer was not found")
    void delete_ThrowsResponseStatusException_WhenProducerNotFound() throws Exception {

        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        Long id = 99L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/producers/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("Producer not found"));

    }

    @Test
    @Order(11)
    @DisplayName("POST v1/producers returns bad request when field name is null")
    void save_ReturnsBadRequest_WhenNameIsNull() throws Exception {

        var request = fileUtils.readResourceFile("producer/post-producer-empty-name-400.json");

        var result = mockMvc.perform(MockMvcRequestBuilders.post("/v1/producers").content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("x-api-key", "1234")
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        var exception = result.getResolvedException();

        Assertions.assertThat(exception).isNotNull();

        Assertions.assertThat(exception.getMessage()).contains("The field 'name' is required");

    }


}