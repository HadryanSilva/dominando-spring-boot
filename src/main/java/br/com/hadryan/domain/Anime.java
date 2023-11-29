package br.com.hadryan.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class Anime {

    private Long id;
    private String name;

    public Anime() {
    }

    public Anime(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore
    public static List<Anime> getAnimes() {
        return List.of(
                new Anime(1, "Naruto"),
                new Anime(2, "Bleach"),
                new Anime(3, "Dragonball Z"));
    }

}
