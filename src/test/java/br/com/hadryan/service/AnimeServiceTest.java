package br.com.hadryan.service;

import br.com.hadryan.commons.AnimeUtils;
import br.com.hadryan.domain.Anime;
import br.com.hadryan.exception.NotFoundException;
import br.com.hadryan.repository.AnimeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AnimeServiceTest {

    @InjectMocks
    private AnimeService service;
    @Mock
    private AnimeRepository repository;
    private List<Anime> animes;
    @InjectMocks
    private AnimeUtils animeUtils;

    @BeforeEach
    void init() {
        animes = animeUtils.newAnimeList();
    }

    @Test
    @DisplayName("findAll() returns a list with all animes when name is null")
    void findAll_ReturnAllAnimes_WhenNameIsNull() {
        BDDMockito.when(repository.findByName(null)).thenReturn(animes);
        var animes = service.findAll(null);

        Assertions.assertThat(animes).hasSize(3).hasSameElementsAs(this.animes);
    }

    @Test
    @DisplayName("findAll() returns the anime found")
    void findAll_ReturnAnime_WhenNameFound() {
        var name = "Naruto";
        var animeExpected = this.animes.stream().filter(anime -> anime.getName().equalsIgnoreCase(name)).toList();
        BDDMockito.when(repository.findByName(name)).thenReturn(animeExpected);
        var animeFound = service.findAll(name);

        Assertions.assertThat(animeFound).hasSize(1).contains(animeExpected.get(0));
    }

    @Test
    @DisplayName("findAll() returns an empty list when name not found")
    void findAll_ReturnsEmptyList_WhenNameNotFound() {
        var name = "x";
        BDDMockito.when(repository.findByName(name)).thenReturn(Collections.emptyList());
        var anime = service.findAll(name);

        Assertions.assertThat(anime).isEmpty();
    }

    @Test
    @DisplayName("findById() returns a anime with given id")
    void findById_ReturnsAnime_WhenSuccessful() {
        var id = 1L;
        var expectedAnime = this.animes.get(0);
        BDDMockito.when(repository.findById(1L)).thenReturn(Optional.of(expectedAnime));
        var animeFound = service.findById(id);

        Assertions.assertThat(animeFound).isEqualTo(expectedAnime);
    }

    @Test
    @DisplayName("findById() returns an empty list when anime not found")
    void findByid_ReturnsEmptyList_WhenAnimeNotFound() {
        BDDMockito.when(repository.findById(1L)).thenReturn(Optional.empty());
        var anime = service.findById(1L);

        Assertions.assertThat(anime).isNull();
    }

    @Test
    @DisplayName("save() returns a saved anime when successful")
    void saveAnime_WhenSuccessful() {
        var anime = animeUtils.newAnimeToSave();
        BDDMockito.when(repository.save(anime)).thenReturn(anime);
        var animeSaved = service.save(anime);

        Assertions.assertThat(animeSaved)
                .isEqualTo(anime)
                .hasNoNullFieldsOrProperties();
    }

    @Test
    @DisplayName("delete() deletes Anime successfully")
    void deleteAnime_WhenSuccessful() {
        var id = 1L;
        var animeToDelete = animes.get(0);
        BDDMockito.when(repository.findById(id)).thenReturn(Optional.of(animeToDelete));
        service.delete(id);

        Assertions.assertThatNoException().isThrownBy(() -> service.delete(id));
    }

    @Test
    @DisplayName("delete() throws NotFoundException when anime not found")
    void deleteThrowsNotFoundException_WhenAnimeNotFound() {
        var id = 1L;
        BDDMockito.when(repository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.delete(id))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("update() returns updated anime when successful")
    void updateAnime_WhenSuccessful() {
        var animeToUpdate = this.animes.get(0);
        BDDMockito.when(repository.save(animeToUpdate)).thenReturn(animeToUpdate);
        animeToUpdate.setName("One Piece");
        service.update(animeToUpdate);

        Assertions.assertThatNoException().isThrownBy(() -> service.update(animeToUpdate));
    }

    @Test
    @DisplayName("update() throws NotFoundException when anime not found")
    void updateThrowsNotFoundException_WhenAnimeNotFound() {
        var id = 1L;
        BDDMockito.when(repository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.delete(id))
                .isInstanceOf(NotFoundException.class);
    }

}