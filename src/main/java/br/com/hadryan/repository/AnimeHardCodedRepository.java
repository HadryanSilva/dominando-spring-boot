package br.com.hadryan.repository;

import br.com.hadryan.domain.Anime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AnimeHardCodedRepository {

    private final AnimeData animeData;

    public List<Anime> findALl() {
        return animeData.getAnimes();
    }

    public Optional<Anime> findById(Long id) {
        return animeData.getAnimes().stream().filter(anime -> anime.getId().equals(id)).findFirst();
    }

    public List<Anime> findByName(String name) {
        return name == null ? animeData.getAnimes() : animeData.getAnimes().stream()
                .filter(anime -> anime.getName().equalsIgnoreCase(name))
                .toList();
    }

    public Anime save(Anime anime) {
        animeData.getAnimes().add(anime);
        return anime;
    }

    public void delete(Anime anime) {
        var animeToDelete = findById(anime.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot found anime to be deleted"));
        animeData.getAnimes().remove(animeToDelete);
    }

    public void update(Anime anime) {
        delete(anime);
        save(anime);
    }

}
