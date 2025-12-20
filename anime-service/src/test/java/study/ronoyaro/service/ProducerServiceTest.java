package study.ronoyaro.service;

import lombok.NonNull;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import study.ronoyaro.commons.MockProducerListUtils;
import study.ronoyaro.domain.Producer;
import study.ronoyaro.repository.ProducerHardCodedRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProducerServiceTest {
    @InjectMocks
    private ProducerService service;
    @Mock
    private ProducerHardCodedRepository repository;
    @InjectMocks
    private MockProducerListUtils producersUtils;

    private List<Producer> producerList;

    @BeforeEach
    void init() {
        producerList = producersUtils.getList();
    }

    @Test
    @Order(1)
    @DisplayName("find All returns a list with all Producers")
    void findAll_ReturnsAnEmptyList_WhenNameIsNull() {

        BDDMockito.when(repository.findAll()).thenReturn(producerList);

        Assertions
                .assertThat(service.findAll(null))
                .hasSameElementsAs(producerList);

    }

    @Test
    @Order(2)
    @DisplayName("find All returns a list with a Producer when exists a name passed by argument")
    void findAll_ReturnsAListProducer_WhenNameIsExists() {
        var producer = producerList.getFirst();

        var producersExpected = singletonList(producer);

        BDDMockito.when(repository.findByName(producer.getName()))
                .thenReturn(producersExpected);

        Assertions
                .assertThat(service.findAll(producer.getName()))
                .containsAll(producersExpected);

    }

    @Test()
    @Order(3)
    @DisplayName("findAll returns a empty list when doesn's exists a producer")
    void findAll_ReturnsAEmptyList_WhenProducerIsNotFound() {

        BDDMockito.when(repository.findByName("xaxa")).thenReturn(Collections.emptyList());
        var producersEmptyExpected = service.findAll("xaxa");
        Assertions.assertThat(producersEmptyExpected)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @Order(4)
    @DisplayName("find by id returns a producer ")
    void findById_ReturnsAProducer_WhenSuccessful() {
        Producer producerExpected = producerList.getFirst();

        BDDMockito.when(repository.findById(producerExpected.getId())).thenReturn(Optional.of(producerExpected));

        Producer producerFound = service.findByIdOrThrowNotFound(producerExpected.getId());

        Assertions.assertThat(producerFound).isEqualTo(producerExpected);
    }

    @Test
    @Order(5)
    @DisplayName("find by id throws a ResponseStatusException when Producer is not found")
    void findById_ThrowsAResponseStatusException_WhenProducerIsNotFound() {

        Producer producerExpected = producerList.getFirst();

        BDDMockito.when(repository.findById(producerExpected.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.findByIdOrThrowNotFound(producerExpected.getId()))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @Order(6)
    @DisplayName("save creates a producer in a list and returns him")
    void save_createsProducer_WhenSuccessful() {

        var producerToSave = Producer.builder()
                .id(1L)
                .name("Mappa")
                .createdAt(LocalDateTime.now()).build();

        BDDMockito.when(repository.save(producerToSave)).thenReturn(producerToSave);

        Producer producerSaved = service.save(producerToSave);
        Assertions.assertThat(producerSaved)
                .isEqualTo(producerToSave)
                .hasNoNullFieldsOrProperties();

    }

    @Test
    @Order(7)
    @DisplayName("delete removes a producer when producer exists in the list")
    void delete_RemoveProducer_WhenSuccessful() {
        Producer producerToDelete = producerList.getFirst();

        BDDMockito.when(repository.findById(producerToDelete.getId())).thenReturn(Optional.of(producerToDelete));

        BDDMockito.doNothing().when(repository).delete(producerToDelete);

        Assertions.assertThatNoException().isThrownBy(() -> service.delete(producerToDelete.getId()));
    }

    @Test
    @Order(8)
    @DisplayName("delete throws a ResponseStatusException when producer was not found")
    void delete_ThrowsResponseStatusException_WhenProducerNotFound() {
        Producer producerToDelete = producerList.getFirst();

        BDDMockito.when(repository.findById(producerToDelete.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException().isThrownBy(() -> service.delete(producerToDelete.getId()));
    }

    @Test
    @Order(9)
    @DisplayName("Update updates a producer")
    void update_UpdatesAProducer_WhenSuccessful() {
        Producer producerToUpdate = producerList.getFirst();

        producerToUpdate.setName("Toei Animation");

        BDDMockito.when(repository.findById(producerToUpdate.getId())).thenReturn(Optional.of(producerToUpdate));

        BDDMockito.doNothing().when(repository).update(producerToUpdate);

        Assertions
                .assertThatNoException()
                .isThrownBy(() -> service.update(producerToUpdate));

    }


    @Test
    @Order(10)
    @DisplayName("Update throws a ResponseStatusException when Producer was not found")
    void update_ThrowsResponseStatusException_WhenProducerNotFound() {
        Producer producerToUpdate = producerList.getFirst();

        producerToUpdate.setName("Toei Animation");

        BDDMockito.when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.update(producerToUpdate))
                .isInstanceOf(ResponseStatusException.class);

    }


    private static @NonNull List<Producer> singletonList(Producer producer) {
        return Collections.singletonList(producer);
    }


}