package br.com.hadryan.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Anime {

    private Long id;
    private String name;

    @JsonIgnore
    public static List<Anime> getAnimes() {
        return List.of(
                new Anime(1L, "Naruto"),
                new Anime(2L, "Bleach"),
                new Anime(3L, "Dragonball Z"));
    }

}
