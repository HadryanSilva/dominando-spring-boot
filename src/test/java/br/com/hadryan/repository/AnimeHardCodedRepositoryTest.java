package br.com.hadryan.repository;

import br.com.hadryan.domain.Anime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class AnimeHardCodedRepositoryTest {

    @InjectMocks
    private AnimeHardCodedRepository repository;
    @Mock
    private AnimeData animeData;
    private List<Anime> animes;

    @BeforeEach
    void init() {
        var naruto = Anime.builder().id(1L).name("Naruto").build();
        var bleach = Anime.builder().id(2L).name("Bleach").build();
        var dragonball = Anime.builder().id(3L).name("Drangonball Z").build();
        animes = new ArrayList<>(List.of(naruto, bleach, dragonball));

        BDDMockito.when(animeData.getAnimes()).thenReturn(animes);
    }

    @Test
    @DisplayName("findAll() returns a list of All animes")
    void findAll_ReturnAllAnimes_WhenSuccessful() {
        var animes = repository.findALl();
        Assertions.assertThat(animes).hasSameElementsAs(this.animes);
    }

    @Test
    @DisplayName("findById() returns an anime with given id")
    void findById_ReturnsAnime_WhenSuccessful() {
       var animeOptional = repository.findById(1L);
       Assertions.assertThat(animeOptional).isPresent().contains(this.animes.get(0));
    }

    @Test
    @DisplayName("findByName() returns all animes when name is null")
    void findByName_ReturnAllAnimes_WhenNameIsNull() {
        var animes = repository.findByName(null);
        Assertions.assertThat(animes).hasSameElementsAs(this.animes);
    }

    @Test
    @DisplayName("findByName() returns list with filtered animes when name is not null")
    void findByName_ReturnsFilteredAnime_WhenSuccessful() {
        var animeFound = repository.findByName("Naruto");
        Assertions.assertThat(animeFound).hasSize(1).contains(animeFound.get(0));
    }

    @Test
    @DisplayName("findByName() returns empty list when anime not found")
    void findByName_ReturnsEmpty_WhenAnimeNotFound() {
        var animes = repository.findByName("Teste");
        Assertions.assertThat(animes).isEmpty();
    }

    @Test
    @DisplayName("save() returns the anime saved when successful")
    void saveAnime_WhenSuccessful() {
        var anime = Anime.builder()
                .id(4L)
                .name("One Piece")
                .build();
        var animeSaved = repository.save(anime);

        Assertions.assertThat(animeSaved).isEqualTo(anime).hasNoNullFieldsOrProperties();

        var animes = repository.findALl();
        Assertions.assertThat(animes).hasSize(4).contains(animeSaved);
    }

    @Test
    @DisplayName("delete() delete the anime with given id when successful")
    void deleteAnime_WhenSuccessful() {
        var animeToBeDeleted = this.animes.get(1);
        repository.delete(animeToBeDeleted);
        Assertions.assertThat(this.animes).doesNotContain(animeToBeDeleted);
    }

    @Test
    @DisplayName("update() updates the anime when successful")
    void updateAnime_WhenSuccessful() {
        var animeToUpdate = this.animes.get(1);
        animeToUpdate.setName("Death Note");
        repository.update(animeToUpdate);

        this.animes.stream()
                .filter(anime -> anime.getId().equals(animeToUpdate.getId()))
                .findFirst()
                .ifPresent(anime -> Assertions.assertThat(anime.getName()).isEqualTo(animeToUpdate.getName()));
    }

}