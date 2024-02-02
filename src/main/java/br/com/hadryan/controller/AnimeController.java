package br.com.hadryan.controller;

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
    private final AnimeService animeService;

    @GetMapping
    public ResponseEntity<List<AnimeGetResponse>> list(@RequestParam(required = false) String name) {
        log.info("Request received to list all animes, param name '{}'", name);

        var animes = animeService.findAll(name);

        var animeGetResponses = mapper.toGetResponses(animes);

        return ResponseEntity.ok(animeGetResponses);
    }

    @GetMapping("{id}")
    public ResponseEntity<AnimeGetResponse> findById(@PathVariable Long id) {
        log.info("Request received find anime by id '{}'", id);
        var animeFound = animeService.findById(id);

        var response = mapper.toGetResponse(animeFound);

        return ResponseEntity.ok(response);
    }

    // Idempotência
    @PostMapping
    public ResponseEntity<AnimePostResponse> save(@RequestBody @Valid AnimePostRequest request) {
        log.info("Request received save anime '{}'", request);

        var anime = mapper.toAnime(request);

        anime = animeService.save(anime);

        var response = mapper.toPostResponse(anime);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        log.info("Request received to delete the anime by id '{}'", id);
        animeService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody @Valid AnimePutRequest request) {
        log.info("Request received to update the anime '{}'", request);

        var animeToUpdate = mapper.toAnime(request);

        animeService.update(animeToUpdate);

        return ResponseEntity.noContent().build();
    }
}
