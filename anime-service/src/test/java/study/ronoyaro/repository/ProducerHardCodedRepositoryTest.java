package study.ronoyaro.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import study.ronoyaro.domain.Producer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProducerHardCodedRepositoryTest {
    @InjectMocks
    private ProducerHardCodedRepository repository;
    @Mock
    private ProducerData producerData;
    private final List<Producer> mockitoProducers = new ArrayList<>();

    @BeforeEach
    void init() {
        var aniplex = Producer.builder()
                .id(1L)
                .name("Aniplex")
                .createdAt(LocalDateTime.now()).build();
        var studioGhibli = Producer.builder()
                .id(2L).name("Studio Ghibli")
                .createdAt(LocalDateTime.now())
                .build();
        var ufotable = Producer.builder()
                .id(3L)
                .name("Ufotable")
                .createdAt(LocalDateTime.now())
                .build();
        mockitoProducers.addAll(List.of(aniplex, studioGhibli, ufotable));
    }

    @Test
    @DisplayName("findAll returns a list with all Producers")
    @Order(1)
    void findAll_ReturnsAllProducers_whenSucessful() {
        BDDMockito.when(producerData.getProducers()).thenReturn(mockitoProducers);
        var producers = repository.findAll();
        Assertions.assertThat(producers)
                .isNotNull()
                .isNotEmpty()
                .hasSize(mockitoProducers.size());
    }

    @Test
    @DisplayName("findById returns a Producer with given Id")
    @Order(2)
    void findById_ReturnsProducer_WhenSucessFul() {
        BDDMockito.when(producerData.getProducers()).thenReturn(mockitoProducers);
        var producerExpected = mockitoProducers.getFirst();
        Assertions.assertThat(repository.findById(producerExpected.getId())).isPresent().contains(producerExpected);
    }

    @Test
    @DisplayName("findByName returns a list empty when the name is Null")
    @Order(3)
    void findByName_ReturnsAnEmptyList_whenNameIsNull() {
        BDDMockito.when(producerData.getProducers()).thenReturn(mockitoProducers);
        var producers = repository.findByName(null);
        Assertions.assertThat(producers).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("findByName returns a list with a Producer when name is given")
    @Order(4)
    void findByName_ReturnsListProducer_whenNameIsFound() {
        BDDMockito.when(producerData.getProducers()).thenReturn(mockitoProducers);

        var producerExpected = mockitoProducers.getFirst();

        var producers = repository.findByName(producerExpected.getName());

        Assertions.assertThat(producers).isNotEmpty().contains(producerExpected);
    }

    @Test
    @DisplayName("Save a producer in a list Producers")
    @Order(5)
    void save_CreatesProducer_WhenSuccessful() {
        BDDMockito.when(producerData.getProducers()).thenReturn(mockitoProducers);

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
        BDDMockito.when(producerData.getProducers()).thenReturn(mockitoProducers);

        var producerToDelete = mockitoProducers.getFirst();

        repository.delete(producerToDelete);

        Assertions.assertThat(mockitoProducers).doesNotContain(producerToDelete);

    }

    @Test
    @DisplayName("Update a producer in a list")
    @Order(7)
    void update_UpdatesProducer_whenSucessful() {
        BDDMockito.when(producerData.getProducers()).thenReturn(mockitoProducers);

        var producerToUpdate = mockitoProducers.getFirst();

        producerToUpdate.setName("Studio Bones");

        repository.update(producerToUpdate);

        Assertions.assertThat(mockitoProducers).contains(producerToUpdate);

        var producerOptional = repository.findById(producerToUpdate.getId());

        Assertions.assertThat(producerOptional).isPresent();

        Assertions.assertThat(producerOptional.get().getName()).isEqualTo(producerToUpdate.getName());

    }
}