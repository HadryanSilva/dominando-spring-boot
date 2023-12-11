package br.com.hadryan.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
public class Anime {

    private Long id;
    private String name;
    private static List<Anime> animes = new ArrayList<>();

    static {
        var naruto = Anime.builder().id(ThreadLocalRandom.current().nextLong(100_000)).name("Naruto").build();
        var bleach = Anime.builder().id(ThreadLocalRandom.current().nextLong(100_000)).name("Bleach").build();
        var dragonball = Anime.builder().id(ThreadLocalRandom.current().nextLong(100_000)).name("Drangonball Z").build();
        animes.addAll(List.of(naruto, bleach, dragonball));
    }

    public static List<Anime> getAnimes() {
        return animes;
    }
}
