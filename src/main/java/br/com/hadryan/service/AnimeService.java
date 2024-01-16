package br.com.hadryan.service;

import br.com.hadryan.domain.Anime;
import br.com.hadryan.exception.NotFoundException;
import br.com.hadryan.repository.AnimeHardCodedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnimeService {

    private final AnimeHardCodedRepository repository;

    public List<Anime> findAll(String name) {
        return repository.findByName(name);
    }

    public Optional<Anime> findById(Long id) {
        return repository.findById(id);
    }

    public Anime save(Anime anime) {
        return repository.save(anime);
    }

    public void delete(Long id) {
        var anime = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cannot found anime to be deleted"));
        repository.delete(anime);
    }

    public void update(Anime animeToUpdate) {
        repository.update(animeToUpdate);
    }

}
