package study.ronoyaro.producer;

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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import study.ronoyaro.commons.FileUtils;
import study.ronoyaro.commons.MockProducerListUtils;
import study.ronoyaro.producer.controller.ProducerController;
import study.ronoyaro.producer.domain.Producer;
import study.ronoyaro.producer.repository.ProducerRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@WebMvcTest(controllers = ProducerController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan(basePackages = {"study.ronoyaro.producer", "study.ronoyaro.commons"})
class ProducerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProducerRepository repository;

    @Autowired
    private MockProducerListUtils producerListUtils;

    private List<Producer> producerList;

    @Autowired
    private FileUtils fileUtils;

    @BeforeEach
    void init() {
        producerList = producerListUtils.getList();
    }


    @Test
    @Order(1)
    @DisplayName("GET /v1/producers returns All Producers when the name Arguments is null")
    void findAll_ReturnsAllProducers_WhenArgumentIsNull() throws Exception {
        BDDMockito.when(repository.findAll()).thenReturn(producerList);

        var response = fileUtils.readResourceFile("producer/get-producer-null-argument-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/producers"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));

    }

    @Test
    @Order(2)
    @DisplayName("GET /v1/producers?name=Ufotable returns a Producer when the name Arguments exists")
    void findAll_ReturnsAllProducers_WhenNameExists() throws Exception {
        var response = fileUtils.readResourceFile("producer/get-producer-ufotable-200.json");
        String name = "Ufotable";

        var producersExpected = producerList.stream()
                .filter(producer -> producer.getName().equalsIgnoreCase(name))
                .toList();

        BDDMockito.when(repository.findByNameIgnoreCase(name)).thenReturn(producersExpected);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/producers").param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));

    }

    @Test
    @Order(3)
    @DisplayName("GET /v1/producers?name=x returns an empty list when the name doesn't exists")
    void findById_ReturnsAnEmptyList_WhenDoesntExists() throws Exception {
        String name = "x";

        var response = fileUtils.readResourceFile("producer/get-producer-empty-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/producers").param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @Order(4)
    @DisplayName("GET /v1/producers/3 id returns an Anime when successful")
    void findById_ReturnsAnAnime_WhenSuccessful() throws Exception {
        var response = fileUtils.readResourceFile("producer/get-producer-by-id-200.json");
        Long id = 3L;

        var producerExpected = producerList.stream()
                .filter(producer -> producer.getId().equals(id))
                .findFirst();

        BDDMockito.when(repository.findById(id)).thenReturn(producerExpected);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/producers/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));

    }

    @Test
    @Order(5)
    @DisplayName("GET /v1/producers/99 throws a NotFoundException when the Anime is not found")
    void findById_ThrowsNotFoundException_WhenAnimeIsNotFound() throws Exception {
        var response = fileUtils.readResourceFile("producer/get-producer-404-response.json");

        Long id = 99L;

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/producers/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
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

        var request = fileUtils.readResourceFile("producer/put-producer-request-200.json");
        Long id = 1L;

        var producerExpected = producerList.stream()
                .filter(producer -> producer.getId().equals(id))
                .findFirst();

        BDDMockito.when(repository.findById(id)).thenReturn(producerExpected);


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
    @DisplayName("PUT /v1/producers throws NotFoundException when Producer is not found")
    void put_ThrowsNotFoundException_WhenProducerIsNotFound() throws Exception {
        var request = fileUtils.readResourceFile("producer/put-producer-not-found-request.json");
        var response = fileUtils.readResourceFile("producer/get-producer-404-response.json");

        mockMvc.perform(MockMvcRequestBuilders.put("/v1/producers")
                        .header("x-api-key", "1234")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @Order(9)
    @DisplayName("DELETE /v1/producers/{id} removes a producer when producer exists in the list")
    void delete_RemoveProducer_WhenSuccessful() throws Exception {

        Long id = 1L;

        var producerExpected = producerList.stream()
                .filter(producer -> producer.getId().equals(id))
                .findFirst();

        BDDMockito.when(repository.findById(id)).thenReturn(producerExpected);

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/producers/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());

    }

    @Test
    @Order(10)
    @DisplayName("DELETE /v1/producers/{id} throws a NotFoundException when producer was not found")
    void delete_ThrowsNotFoundException_WhenProducerNotFound() throws Exception {

        var response = fileUtils.readResourceFile("producer/get-producer-404-response.json");

        Long id = 99L;


        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/producers/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));


    }

    @ParameterizedTest
    @Order(12)
    @MethodSource(value = "postBadRequestSource")
    @DisplayName("POST /v1/producers throws a Bad Request when the field name is empty")
    void post_ThrowsBadRequest_WhenTheFieldNameIsEmpty(String path, List<String> errors) throws Exception {

        var request = fileUtils.readResourceFile(path);

        var result = mockMvc.perform(MockMvcRequestBuilders.post("/v1/producers")
                        .header("x-api-key", "1234")
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
    @Order(13)
    @MethodSource(value = "putBadRequestSource")
    @DisplayName("PUT /v1/producers throws a Bad Request when the field name is empty")
    void put_ThrowsBadRequest_WhenTheFieldIdAndNameIsEmpty(String path, List<String> errors) throws Exception {

        var request = fileUtils.readResourceFile(path);

        var result = mockMvc.perform(MockMvcRequestBuilders.put("/v1/producers")
                        .header("x-api-key", "1234")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        var exception = result.getResolvedException();

        Assertions.assertThat(exception.getMessage()).contains(errors);
    }


    private static Stream<Arguments> postBadRequestSource() {

        String name = "The field 'name' is required";

        var errors = Collections.singletonList(name);

        return Stream.of(
                Arguments.of("producer/post-producer-empty-name-400.json", errors)
        );
    }

    private static Stream<Arguments> putBadRequestSource() {
        String id = "The field 'id' cannot be null";
        String name = "The field 'name' is required";

        var errors = List.of(id, name);
        var onlyNameEmpty = Collections.singletonList(name);

        return Stream.of(
                Arguments.of("producer/put-producer-id-and-name-empty-400.json", errors),
                Arguments.of("producer/put-producer-name-empty-400.json", onlyNameEmpty)
        );

    }


}