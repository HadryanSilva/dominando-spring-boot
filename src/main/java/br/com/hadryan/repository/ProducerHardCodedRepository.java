package br.com.hadryan.repository;

import br.com.hadryan.domain.Producer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ProducerHardCodedRepository {

    private static List<Producer> PRODUCERS = new ArrayList<>();

    static {
        var toei = Producer.builder().id(1L).name("Toei Animation").createdAt(LocalDateTime.now()).build();
        var whatever = Producer.builder().id(2L).name("Whatever").createdAt(LocalDateTime.now()).build();
        PRODUCERS.addAll(List.of(toei, whatever));
    }

    public Optional<Producer> findById(long id) {
        return PRODUCERS
                .stream()
                .filter(producer -> producer.getId().equals(id))
                .findFirst();
    }

    public List<Producer> findByName(String name) {
        return name == null ? PRODUCERS : PRODUCERS
                .stream()
                .filter(producer -> producer.getName().equalsIgnoreCase(name))
                .toList();
    }

    public Producer save(Producer producer) {
        PRODUCERS.add(producer);
        return producer;
    }

    public void delete(Producer producer) {
        var producerToRemove = findById(producer.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot found producer to be deleted"));
        PRODUCERS.remove(producerToRemove);
    }

    public void update(Producer producer) {
        delete(producer);
        save(producer);
    }

}
