package br.com.hadryan.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Anime {

    private Long id;
    private String name;
    private static List<Anime> animes = new ArrayList<>();

    static {
        var naruto = new Anime(1L, "Naruto");
        var bleach = new Anime(2L, "Bleach");
        var dragonball = new Anime(3L, "Dragonball Z");
        animes.addAll(List.of(naruto, bleach, dragonball));
    }

    public static List<Anime> getAnimes() {
        return animes;
    }
}
