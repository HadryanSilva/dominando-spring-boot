package br.com.hadryan.repository;

import br.com.hadryan.domain.Anime;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AnimeData {

    private List<Anime> animes = new ArrayList<>();

     {
        var naruto = Anime.builder().id(1L).name("Naruto").build();
        var bleach = Anime.builder().id(2L).name("Bleach").build();
        var dragonball = Anime.builder().id(3L).name("Drangonball Z").build();
        animes.addAll(List.of(naruto, bleach, dragonball));
    }

    public List<Anime> getAnimes() {
        return animes;
    }
}
