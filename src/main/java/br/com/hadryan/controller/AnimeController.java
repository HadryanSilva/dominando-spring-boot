package br.com.hadryan.controller;

import br.com.hadryan.domain.Anime;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"v1/animes", "v1/animes/"})
@Log4j2
public class AnimeController {

    @GetMapping

    public List<Anime> listAll() {
        return Anime.getAnimes();
    }

    @GetMapping("filter")
    public List<Anime> filter(@RequestParam(name = "name") String name) {
        log.info("Request recevied to list all animes, params '{}'", name);
        var animes = Anime.getAnimes();
        if (name == null) return animes;
        return animes
                .stream()
                .filter(anime -> anime.getName().equalsIgnoreCase(name))
                .toList();
    }

    @GetMapping("{id}")
    public Anime findByName(@PathVariable Long id) {
        log.info("Request recevied to find animes by id '{}'", id);
        return Anime.getAnimes().stream()
                .filter(n -> n.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

}
