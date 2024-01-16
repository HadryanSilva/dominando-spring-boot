package br.com.hadryan.service;

import br.com.hadryan.commons.ProducerUtils;
import br.com.hadryan.domain.Producer;
import br.com.hadryan.exception.NotFoundException;
import br.com.hadryan.repository.ProducerHardCodedRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ProducerServiceTest {

    @InjectMocks
    private ProducerService service;
    @Mock
    private ProducerHardCodedRepository repository;
    private List<Producer> producers;
    @InjectMocks
    private ProducerUtils producerUtils;

    @BeforeEach
    void init() {
        producers = producerUtils.newProducerList();
    }

    @Test
    @DisplayName("findAll() returns a list with all producers")
    void findAll_ReturnsAllProducers_WhenSuccessful() {
        BDDMockito.when(repository.findByName(null)).thenReturn(this.producers);
        var producers = service.findAll(null);

        Assertions.assertThat(producers).hasSameElementsAs(this.producers);
    }

    @Test
    @DisplayName("findAll() returns list with filtered producers when name is not null")
    void findAll_ReturnsFilteredProducer_WhenNameIsNotNull() {
        var name = "Wit Studio";
        var producerFound = this.producers.stream()
                .filter(producer -> producer.getName().equalsIgnoreCase(name)).toList();
        BDDMockito.when(repository.findByName(name)).thenReturn(producerFound);
        var producers = service.findAll(name);

        Assertions.assertThat(producers).hasSize(1).contains(producerFound.get(0));
    }

    @Test
    @DisplayName("findAll() returns a empty list with all producers")
    void findAll_ReturnsEmptyList_WhoNonNameIsFound() {
        var name = "x";
        BDDMockito.when(repository.findByName(name)).thenReturn(Collections.emptyList());
        var producers = service.findAll(name);

        Assertions.assertThat(producers).isEmpty();
    }

    @Test
    @DisplayName("findAll() returns producer when given id exists")
    void findById_ReturnsProducer_WhenSuccessful() {
        var id = 1L;
        var expectedProducer = this.producers.get(0);
        BDDMockito.when(repository.findById(id)).thenReturn(Optional.of(expectedProducer));
        var producer = service.findById(id);

        Assertions.assertThat(producer).isPresent().contains(expectedProducer);
    }

    @Test
    @DisplayName("findAll() returns producer when given id does not exists")
    void findById_ReturnsEmpty_WhenIdDoesNotExist() {
        var id = 1L;
        BDDMockito.when(repository.findById(id)).thenReturn(Optional.empty());
        var producer = service.findById(id);

        Assertions.assertThat(producer).isEmpty();
    }

    @Test
    @DisplayName("save() returns producer saved")
    void saveProducer_WhenSuccessful() {
        var producerToBeSaved = producerUtils.newProducerToSave();
        BDDMockito.when(repository.save(producerToBeSaved)).thenReturn(producerToBeSaved);
        var producer = service.save(producerToBeSaved);

        Assertions.assertThat(producer)
                .isEqualTo(producerToBeSaved)
                .hasNoNullFieldsOrProperties();
    }

    @Test
    @DisplayName("delete() deletes producer with given id")
    void deleteProducer_WhenSuccessful() {
        var id = 1L;
        var producerToBeDeleted = this.producers.get(0);
        BDDMockito.when(repository.findById(id)).thenReturn(Optional.of(producerToBeDeleted));
        BDDMockito.doNothing().when(repository).delete(producerToBeDeleted);

        Assertions.assertThatNoException().isThrownBy(() -> service.delete(id));
    }

    @Test
    @DisplayName("delete() throw ResponseStatusException when producer not found")
    void delete_RemovesThrowResponseStatusException_WhenProducerNotFound() {
        var id = 1L;
        BDDMockito.when(repository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.delete(id))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("update() updates the given producer")
    void updateProducer_WhenSucessful() {
        var producerToBeUpdated = this.producers.get(0);
        var id = 1L;
        BDDMockito.when(repository.findById(id)).thenReturn(Optional.of(producerToBeUpdated));
        BDDMockito.doNothing().when(repository).update(producerToBeUpdated);
        producerToBeUpdated.setName("Aniplex");
        service.update(producerToBeUpdated);

        Assertions.assertThatNoException().isThrownBy(() -> service.update(producerToBeUpdated));
    }

    @Test
    @DisplayName("update() throw ResponseStatusException when producer not found")
    void update_UpdatesThrowResponseStatusException_WhenProducerNotFound() {
        var id = 4L;
        var producerToUpdate = Producer.builder().id(id).name("Teste").build();
        BDDMockito.when(repository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.update(producerToUpdate))
                .isInstanceOf(NotFoundException.class);
    }

}