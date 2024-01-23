package br.com.hadryan.controller;

import br.com.hadryan.exception.NotFoundException;
import br.com.hadryan.mapper.AnimeMapper;
import br.com.hadryan.request.AnimePostRequest;
import br.com.hadryan.request.AnimePutRequest;
import br.com.hadryan.response.AnimeGetResponse;
import br.com.hadryan.response.AnimePostResponse;
import br.com.hadryan.service.AnimeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"v1/animes", "v1/animes/"})
@Log4j2
@RequiredArgsConstructor
public class AnimeController {

    private final AnimeMapper mapper;

    private final AnimeService service;
    
    @GetMapping
    public ResponseEntity<List<AnimeGetResponse>> findAll(@RequestParam(required = false) String name) {
        log.info("Request received to list all animes, params '{}'", name);
        var animes = mapper.toAnimesGetResponse(service.findAll(name));
        if (name == null) return ResponseEntity.ok(animes);
        var animeFound = animes
                .stream()
                .filter(anime -> anime.getName().equalsIgnoreCase(name))
                .toList();
        
        return ResponseEntity.ok(animeFound);
    }

    @GetMapping("{id}")
    public ResponseEntity<AnimeGetResponse> findById(@PathVariable Long id) {
        log.info("Request received to find animes by id '{}'", id);
        var animeFound = service.findById(id).stream()
                .filter(anime -> anime.getId().equals(id)).findFirst()
                .orElseThrow(() -> new NotFoundException("Anime not found"));
        var response = mapper.toGetResponse(animeFound);
        
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<AnimePostResponse> save(@RequestBody @Valid AnimePostRequest request) {
        var anime = mapper.toAnime(request);
        service.save(anime);
        var response = mapper.toPostResponse(anime);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Request received to delete producer with id '{}'", id);
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    @PutMapping
    public ResponseEntity<Void> update(@RequestBody @Valid AnimePutRequest request) {
        log.info("Request received to update anime '{}'", request);
        var animeToUpdate = mapper.toAnime(request);
        service.update(animeToUpdate);
        return ResponseEntity.noContent().build();
    }

}
