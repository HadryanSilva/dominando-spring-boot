package br.com.hadryan.repository;

import br.com.hadryan.domain.Producer;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProducerData {

    private List<Producer> producers = new ArrayList<>();

    {
        var toei = Producer.builder().id(1L).name("Toei Animation").createdAt(LocalDateTime.now()).build();
        var whatever = Producer.builder().id(2L).name("Whatever").createdAt(LocalDateTime.now()).build();
        producers.addAll(List.of(toei, whatever));
    }

    public List<Producer> getProducers() {
        return producers;
    }
}
