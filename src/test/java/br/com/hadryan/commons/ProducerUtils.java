package br.com.hadryan.commons;

import br.com.hadryan.domain.Producer;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProducerUtils {

    public List<Producer> newProducerList() {
        var toei = Producer.builder().id(1L).name("Ufotable").createdAt(LocalDateTime.now()).build();
        var witStudio = Producer.builder().id(2L).name("Wit Studio").createdAt(LocalDateTime.now()).build();
        var studioGhibli = Producer.builder().id(3L).name("Studio Ghibli").createdAt(LocalDateTime.now()).build();
        return new ArrayList<>(List.of(toei, witStudio, studioGhibli));
    }

    public Producer newProducerToSave() {
        return Producer.builder()
                .id(34219L)
                .name("Aniplex")
                .createdAt(LocalDateTime.now())
                .build();
    }
}
