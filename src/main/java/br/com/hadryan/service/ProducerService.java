package br.com.hadryan.service;

import br.com.hadryan.domain.Producer;
import br.com.hadryan.repository.ProducerHardCodedRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

public class ProducerService {

    private ProducerHardCodedRepository repository;

    public ProducerService() {
        this.repository = new ProducerHardCodedRepository();
    }

    public List<Producer> findAll(String name) {
        return repository.findByName(name);
    }

    public Producer save(Producer producer) {
        return repository.save(producer);
    }

    public Optional<Producer> findById(Long id) {
        return repository.findById(id);
    }

    public void delete(Long id) {
        var producer = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot found producer to be deleted"));
        repository.delete(producer);
    }

    public void update(Producer producerToUpdate) {
        var producer = repository.findById(producerToUpdate.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot found producer to be updated"));
        producerToUpdate.setCreatedAt(producer.getCreatedAt());
        repository.update(producerToUpdate);
    }

}
