package br.com.hadryan.commons;

import br.com.hadryan.domain.Anime;

import java.util.ArrayList;
import java.util.List;

public class AnimeUtils {

    public List<Anime> newAnimeList() {
        var naruto = Anime.builder().id(1L).name("Naruto").build();
        var bleach = Anime.builder().id(2L).name("Bleach").build();
        var dragonball = Anime.builder().id(3L).name("Dragon Ball Z").build();
        return new ArrayList<>(List.of(naruto, bleach, dragonball));
    }

    public Anime newAnimeToSave() {
        return Anime.builder()
                .id(4L)
                .name("One Piece")
                .build();
    }
}
