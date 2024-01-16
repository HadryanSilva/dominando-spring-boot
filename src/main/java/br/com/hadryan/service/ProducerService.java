package br.com.hadryan.service;

import br.com.hadryan.domain.Producer;
import br.com.hadryan.exception.NotFoundException;
import br.com.hadryan.repository.ProducerHardCodedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProducerService {

    private final ProducerHardCodedRepository repository;

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
        var producer = findById(id)
                .orElseThrow(() -> new NotFoundException("Cannot found producer to be deleted"));
        repository.delete(producer);
    }

    public void update(Producer producerToUpdate) {
        var producer = repository.findById(producerToUpdate.getId())
                .orElseThrow(() -> new NotFoundException("Cannot found producer to be updated"));
        producerToUpdate.setCreatedAt(producer.getCreatedAt());
        repository.update(producerToUpdate);
    }

}
