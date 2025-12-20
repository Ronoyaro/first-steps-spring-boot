package study.ronoyaro.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import study.ronoyaro.commons.MockProducerListUtils;
import study.ronoyaro.domain.Producer;

import java.time.LocalDateTime;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProducerHardCodedRepositoryTest {
    @InjectMocks
    private ProducerHardCodedRepository repository;
    @Mock
    private ProducerData producerData;
    @InjectMocks
    private MockProducerListUtils producerListUtils;

    private List<Producer> producerList;

    @BeforeEach
    void init() {
        producerList = producerListUtils.getList();
    }

    @Test
    @DisplayName("findAll returns a list with all Producers")
    @Order(1)
    void findAll_ReturnsAllProducers_whenSucessful() {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);
        var producers = repository.findAll();
        Assertions.assertThat(producers)
                .isNotNull()
                .isNotEmpty()
                .hasSize(producerList.size());
    }

    @Test
    @DisplayName("findById returns a Producer with given Id")
    @Order(2)
    void findById_ReturnsProducer_WhenSucessFul() {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);
        var producerExpected = producerList.getFirst();
        var producerFound = repository.findById(producerExpected.getId());
        Assertions.assertThat(producerFound).isPresent().contains(producerExpected);
    }

    @Test
    @DisplayName("findByName returns a list empty when the name is Null")
    @Order(3)
    void findByName_ReturnsAnEmptyList_whenNameIsNull() {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);
        var producers = repository.findByName(null);
        Assertions.assertThat(producers).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("findByName returns a list with a Producer when name is given")
    @Order(4)
    void findByName_ReturnsListProducer_whenNameIsFound() {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        var producerExpected = producerList.getFirst();

        var producers = repository.findByName(producerExpected.getName());

        Assertions.assertThat(producers).isNotEmpty().contains(producerExpected);
    }

    @Test
    @DisplayName("Save a producer in a list Producers")
    @Order(5)
    void save_CreatesProducer_WhenSuccessful() {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        var producedToSave = Producer.builder()
                .id(10L)
                .name("Pierrot Studio")
                .createdAt(LocalDateTime.now())
                .build();

        var producerSaved = repository.save(producedToSave);

        var producerOptional = repository.findById(producerSaved.getId());

        Assertions.assertThat(producerSaved).isEqualTo(producedToSave).hasNoNullFieldsOrProperties();

        Assertions.assertThat(producerOptional).isPresent().contains(producedToSave);

    }

    @Test
    @DisplayName("Delete a producer in a list")
    @Order(6)
    void delete_RemovesProducer_WhenSuccessful() {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        var producerToDelete = producerList.getFirst();

        repository.delete(producerToDelete);

        Assertions.assertThat(producerList).doesNotContain(producerToDelete);

    }

    @Test
    @DisplayName("Update a producer in a list")
    @Order(7)
    void update_UpdatesProducer_whenSucessful() {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        var producerToUpdate = producerList.getFirst();

        producerToUpdate.setName("Studio Bones");

        repository.update(producerToUpdate);

        Assertions.assertThat(producerList).contains(producerToUpdate);

        var producerOptional = repository.findById(producerToUpdate.getId());

        Assertions.assertThat(producerOptional).isPresent();

        Assertions.assertThat(producerOptional.get().getName()).isEqualTo(producerToUpdate.getName());

    }
}