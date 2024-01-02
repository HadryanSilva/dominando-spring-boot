package br.com.hadryan.repository;

import br.com.hadryan.domain.Producer;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;
import test.outside.Connection;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Log4j2
public class ProducerHardCodedRepository {

    private final ProducerData producerData;
    @Qualifier(value = "connectionMySql")
    private final Connection connection;

    public List<Producer> findAll() {
        return producerData.getProducers();
    }

    public Optional<Producer> findById(long id) {
        return producerData.getProducers()
                .stream()
                .filter(producer -> producer.getId().equals(id))
                .findFirst();
    }

    public List<Producer> findByName(String name) {
        log.info(connection);
        return name == null ? producerData.getProducers() : producerData.getProducers()
                .stream()
                .filter(producer -> producer.getName().equalsIgnoreCase(name))
                .toList();
    }

    public Producer save(Producer producer) {
        producerData.getProducers().add(producer);
        return producer;
    }

    public void delete(Producer producer) {
        var producerToRemove = findById(producer.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot found producer to be deleted"));
        var isRemoved = producerData.getProducers().remove(producerToRemove);
        log.info(isRemoved);
    }

    public void update(Producer producer) {
        delete(producer);
        save(producer);
    }

}
