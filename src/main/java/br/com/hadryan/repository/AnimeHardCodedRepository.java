package br.com.hadryan.repository;

import br.com.hadryan.domain.Anime;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AnimeHardCodedRepository {

    private static List<Anime> ANIMES = new ArrayList<>();

    static {
        var naruto = Anime.builder().id(1L).name("Naruto").build();
        var bleach = Anime.builder().id(2L).name("Bleach").build();
        var dragonball = Anime.builder().id(3L).name("Drangonball Z").build();
        ANIMES.addAll(List.of(naruto, bleach, dragonball));
    }

    public List<Anime> findALl() {
        return ANIMES;
    }

    public Optional<Anime> findById(Long id) {
        return ANIMES.stream().filter(anime -> anime.getId().equals(id)).findFirst();
    }

    public List<Anime> findByName(String name) {
        return name == null ? ANIMES : ANIMES.stream()
                .filter(anime -> anime.getName().equalsIgnoreCase(name))
                .toList();
    }

    public Anime save(Anime anime) {
        ANIMES.add(anime);
        return anime;
    }

    public void delete(Anime anime) {
        var animeToDelete = findById(anime.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot found anime to be deleted"));
        ANIMES.remove(animeToDelete);
    }

    public void update(Anime anime) {
        delete(anime);
        save(anime);
    }

}
