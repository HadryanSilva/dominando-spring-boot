package br.com.hadryan.controller;

import br.com.hadryan.AnimeMapper;
import br.com.hadryan.domain.Anime;
import br.com.hadryan.request.AnimePostRequest;
import br.com.hadryan.request.AnimePutRequest;
import br.com.hadryan.response.AnimeGetResponse;
import br.com.hadryan.response.AnimePostResponse;
import br.com.hadryan.service.AnimeService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping({"v1/animes", "v1/animes/"})
@Log4j2
public class AnimeController {

    private final AnimeMapper MAPPER = AnimeMapper.INSTANCE;

    private AnimeService service;

    public AnimeController() {
        this.service = new AnimeService();
    }
    
    @GetMapping
    public ResponseEntity<List<AnimeGetResponse>> list(@RequestParam(required = false) String name) {
        log.info("Request recevied to list all animes, params '{}'", name);
        var animes = MAPPER.toAnimesGetResponse(service.findAll(name));
        if (name == null) return ResponseEntity.ok(animes);
        var animeFound = animes
                .stream()
                .filter(anime -> anime.getName().equalsIgnoreCase(name))
                .toList();
        
        return ResponseEntity.ok(animeFound);
    }

    @GetMapping("{id}")
    public ResponseEntity<AnimeGetResponse> findById(@PathVariable Long id) {
        log.info("Request recevied to find animes by id '{}'", id);
        var animeFound = service.findById(id).stream()
                .filter(anime -> anime.getId().equals(id)).findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        var response = MAPPER.toGetResponse(animeFound);
        
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<AnimePostResponse> save(@RequestBody AnimePostRequest request) {
        var anime = MAPPER.toAnime(request);
        service.save(anime);
        var response = MAPPER.toPostResponse(anime);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Request recevied to delete producer with id '{}'", id);
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    @PutMapping
    public ResponseEntity<Void> update(@RequestBody AnimePutRequest request) {
        log.info("Request recevied to update anime '{}'", request);
        var animeToUpdate = MAPPER.toAnime(request);
        service.update(animeToUpdate);
        return ResponseEntity.noContent().build();
    }
}
