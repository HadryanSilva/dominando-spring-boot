package br.com.hadryan.repository;

import br.com.hadryan.domain.Producer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class ProducerHardCodedRepositoryTest {

    @InjectMocks
    private ProducerHardCodedRepository repository;
    @Mock
    private ProducerData producerData;
    private List<Producer> producers;

    @BeforeEach
    void init() {
        var toei = Producer.builder().id(1L).name("Ufotable").createdAt(LocalDateTime.now()).build();
        var witStudio = Producer.builder().id(2L).name("Wit Studio").createdAt(LocalDateTime.now()).build();
        var studioGhibli = Producer.builder().id(3L).name("Studio Ghibli").createdAt(LocalDateTime.now()).build();
        producers = new ArrayList<>(List.of(toei, witStudio, studioGhibli));

        BDDMockito.when(producerData.getProducers()).thenReturn(producers);
    }

    @Test
    @DisplayName("findAll() returns a list with all producers")
    void findAll_ReturnsAllProducers_WhenSuccessful() {
        var producers = repository.findAll();
        Assertions.assertThat(producers).hasSameElementsAs(this.producers);
    }

    @Test
    @DisplayName("findById() returns an object whit given id")
    void findById_ReturnsAProducer_WhenSuccessful() {
        var producerOptional = repository.findById(3L);
        Assertions.assertThat(producerOptional).isPresent().contains(producers.get(2));
    }

    @Test
    @DisplayName("findByName() returns all producers when name is null")
    void findByName_ReturnsAllProducer_WhenNameIsNull() {
        var producers = repository.findByName(null);
        Assertions.assertThat(producers).hasSameElementsAs(this.producers);
    }

    @Test
    @DisplayName("findByName() returns list with filtered producers when name is not null")
    void findByName_ReturnsFilteredProducer_WhenNameIsNotNull() {
        var producers = repository.findByName("Wit Studio");
        Assertions.assertThat(producers).hasSize(1).contains(this.producers.get(1));
    }

    @Test
    @DisplayName("findByName() returns empty list when no producer is found")
    void findByName_ReturnsEmptyList_WhenNoProducerFound() {
        var producers = repository.findByName("Toei Animation");
        Assertions.assertThat(producers).isEmpty();
    }

    @Test
    @DisplayName("save() creates a new producer")
    void save_CreatesProducer_WhenSucessfull() {
        var producerToBeSaved = Producer.builder()
                .id(99L)
                .name("MAPPA")
                .createdAt(LocalDateTime.now())
                .build();

        var producer = repository.save(producerToBeSaved);
        Assertions.assertThat(producer)
                .isEqualTo(producerToBeSaved)
                .hasNoNullFieldsOrProperties();

        var producers = repository.findAll();
        Assertions.assertThat(producers).contains(producerToBeSaved);
    }

    @Test
    @DisplayName("delete() removes a producer")
    void delete_DeletesProducer_WhenSucessfull() {
        var producerToBeDeleted = this.producers.get(1);
        repository.delete(producerToBeDeleted);
        Assertions.assertThat(this.producers).doesNotContain(producerToBeDeleted);
    }

    @Test
    @DisplayName("update() updates a producer")
    void update_UpdatesProducer_WhenSucessfull() {
        var producerToBeUpdated = this.producers.get(0);
        producerToBeUpdated.setName("Aniplex");
        repository.update(producerToBeUpdated);
        Assertions.assertThat(this.producers).contains(producerToBeUpdated);

        this.producers
                .stream()
                .filter(producer -> producer.getId().equals(producerToBeUpdated.getId()))
                .findFirst()
                .ifPresent(producer -> Assertions.assertThat(producer.getName()).isEqualTo(producerToBeUpdated.getName()));
    }
}